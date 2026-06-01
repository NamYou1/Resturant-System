package com.saranaresturantsystem.services.impl.inventory;

import com.saranaresturantsystem.common.InvoiceService;
import com.saranaresturantsystem.dto.request.inventory.TransferItemRequest;
import com.saranaresturantsystem.dto.request.inventory.TransferRequest;
import com.saranaresturantsystem.dto.response.inventory.TransferResponse;
import com.saranaresturantsystem.entities.inventory.Store;
import com.saranaresturantsystem.entities.inventory.Transfer;
import com.saranaresturantsystem.entities.inventory.TransferItem;
import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.enums.SaleStatus;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.enums.TransactionType;
import com.saranaresturantsystem.enums.TransferStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.execption.ApiException;
import org.springframework.http.HttpStatus;
import com.saranaresturantsystem.mappers.inventory.TransferMapper;
import com.saranaresturantsystem.repositories.inventory.TransferRepository;
import com.saranaresturantsystem.services.*;
import com.saranaresturantsystem.specification.inventory.transfer.TransferFilter;
import com.saranaresturantsystem.specification.inventory.transfer.TransferSpec;
import com.saranaresturantsystem.utils.PageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;
    private  final StockService stockService ;
    private final ProductService productService;
    private  final StoreService storeService ;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper ;
    private  final InvoiceService invoiceService ;

    // CREATE
    @Override
    public TransferResponse create(TransferRequest request) {
        Transfer transfer = transferMapper.toEntity(request);
        transfer.setStatus(TransferStatus.PENDING);
        transfer.setTransferNo(invoiceService.generate("TRF"));
        transfer.setCreatedAt(LocalDateTime.now());
        Store fromStoreId = storeService.findById(request.getFromStoreId());
        Store toStoreId = storeService.findById(request.getToStoreId());
        if (Objects.equals(fromStoreId.getId(), toStoreId.getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "From and To store cannot be the same");
        }
//        if (Object.E)
        BigDecimal total = BigDecimal.ZERO ;
        List<TransferItem> items = new ArrayList<>();

        for (TransferItemRequest itemRequest : request.getItems()) {

            Product product = productService.getProductById(itemRequest.getProductId());
            TransferItem item = transferMapper.toItemRequest(itemRequest);
            item.setTransfer(transfer);
            item.setCostPrice(product.getCostPrice());
            item.setProduct(product);
//            item.setProduct(product.getId());
            item.setSubtotal(item.getQuantity().multiply(product.getCostPrice()));
            total = total.add(item.getSubtotal());
            items.add(item);
        }
        transfer.setItems(items);
        transfer.setTotal(total);
        transfer.setGrandTotal(total);
        Transfer saved = transferRepository.save(transfer);
        return transferMapper.toResponse(saved);
    }

    // GET
    @Override
    @Transactional
    public TransferResponse getById(Long id) {
        return transferMapper.toResponse(find(id));
    }
    // LIST
    @Override
    @Transactional
    public Page<TransferResponse> getAll(Map<String, String> params) {
        TransferFilter filter = objectMapper.convertValue(params, TransferFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<Transfer> spec = TransferSpec.filterBy(filter);
        return  transferRepository.findAll(spec, pageable).map(transferMapper::toResponse);
    }

    // UPDATE
    @Override
    public TransferResponse update(Long id, TransferRequest request, String updatedBy) {
        Transfer transfer = find(id);
        if (transfer.getStatus() != TransferStatus.PENDING) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only PENDING transfer can be updated");
        }

        transferMapper.updateEntity(request, transfer);
        transfer.setUpdatedBy(updatedBy);
        transfer.setUpdatedAt(LocalDateTime.now());

        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    // APPROVE
    @Override
//    @CacheEvict(cacheNames = {"transfer-page", "transfer-response", "txn-summary"}, allEntries = true)
    public TransferResponse approve(Long id, String updatedBy) {

        Transfer transfer = find(id);
        if (transfer.getStatus() != TransferStatus.PENDING) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only PENDING transfer can be approved");
        }

        transfer.setStatus(TransferStatus.APPROVED);
        transfer.setUpdatedBy(updatedBy);
        transfer.setUpdatedAt(LocalDateTime.now());

        return transferMapper.toResponse(
                transferRepository.save(transfer)
        );
    }

    @Override
    public TransferResponse complete(Long id, String updatedBy) {
        Transfer transfer = find(id);
        // Allow completing a transfer that is still PENDING by auto‑approving it
        if (transfer.getStatus() != TransferStatus.APPROVED) {
            if (transfer.getStatus() == TransferStatus.PENDING) {
                // Auto‑approve before completing
                transfer.setStatus(TransferStatus.APPROVED);
                transfer.setUpdatedBy(updatedBy);
                transfer.setUpdatedAt(LocalDateTime.now());
            } else {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Only APPROVED transfer can complete");
            }
        }
        for (TransferItem item : transfer.getItems()) {
            stockService.transferStock(
                    item.getProduct().getId(),
                    transfer.getFromStoreId().getId(),
                    transfer.getToStoreId().getId(),
                    item.getQuantity()
            );

            Long unitId = resolveUnitId(item);

            transactionService.logStockMovement(
                    TransactionType.TRANSFER_OUT,
                    transfer.getId(),
                    transfer.getTransferNo(),
                    item.getProduct().getId(),
                    transfer.getFromStoreId().getId(),
                    unitId,
                    item.getQuantity(),
                    item.getUnitQuantity(),
                    item.getCostPrice(),
                    SaleStatus.COMPLETED,
                    updatedBy
            );

            transactionService.logStockMovement(
                    TransactionType.TRANSFER_IN,
                    transfer.getId(),
                    transfer.getTransferNo(),
                    item.getProduct().getId(),
                    transfer.getToStoreId().getId(),
                    unitId,
                    item.getQuantity(),
                    item.getUnitQuantity(),
                    item.getCostPrice(),
                    SaleStatus.COMPLETED,
                    updatedBy
            );
        }
        transfer.setStatus(TransferStatus.COMPLETED);
        transfer.setUpdatedBy(updatedBy);

        transfer.setUpdatedAt(LocalDateTime.now());

        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    // CANCEL
    @Override
//    @CacheEvict(cacheNames = {"transfer-page", "transfer-response", "txn-summary"}, allEntries = true)
    public TransferResponse cancel(Long id, String updatedBy) {

        Transfer transfer = find(id);

        if (transfer.getStatus() == TransferStatus.CANCELLED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Transfer already cancelled");
        }

        if (transfer.getStatus() == TransferStatus.COMPLETED) {
            for (TransferItem item : transfer.getItems()) {
                stockService.reverseStock(
                        item.getProduct().getId(),
                        transfer.getFromStoreId().getId(),
                        transfer.getToStoreId().getId(),
                        item.getQuantity()
                );

                transactionService.logStockMovement(
                        TransactionType.TRANSFER_OUT,
                        transfer.getId(),
                        transfer.getTransferNo(),
                        item.getProduct().getId(),
                        transfer.getToStoreId().getId(),
                        resolveUnitId(item),
                        item.getQuantity(),
                        item.getUnitQuantity(),
                        item.getCostPrice(),
                        SaleStatus.CANCELLED,
                        updatedBy
                );

                transactionService.logStockMovement(
                        TransactionType.TRANSFER_IN,
                        transfer.getId(),
                        transfer.getTransferNo(),
                        item.getProduct().getId(),
                        transfer.getFromStoreId().getId(),
                        resolveUnitId(item),
                        item.getQuantity(),
                        item.getUnitQuantity(),
                        item.getCostPrice(),
                        SaleStatus.CANCELLED,
                        updatedBy
                );
            }
        }

        transfer.setStatus(TransferStatus.CANCELLED);
        transfer.setUpdatedBy(updatedBy);
        transfer.setUpdatedAt(LocalDateTime.now());

        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    // DELETE (SOFT)
    @Override
    public void delete(Long id, String deletedBy) {

        Transfer transfer = find(id);
        if (transfer.getDeletedAt() != null || transfer.getIsActive() == StatusType.INACTIVE) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Transfer already deleted");
        }

        if (transfer.getStatus() == TransferStatus.COMPLETED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot delete completed transfer");
        }
        transfer.setIsActive(StatusType.INACTIVE);
        transfer.setDeletedBy(deletedBy);
        transfer.setDeletedAt(LocalDateTime.now());

        transferRepository.save(transfer);
    }

    private Transfer find(Long id) {
        return transferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transfer  " , id));
    }

    private Long resolveUnitId(TransferItem item) {
        // Prefer the unit defined on the transfer line
        if (item.getUnit() != null && item.getUnit().getId() != null) {
            return item.getUnit().getId();
        }
        // Fall back to the product's default unit
        if (item.getProduct() != null && item.getProduct().getUnit() != null && item.getProduct().getUnit().getId() != null) {
            return item.getProduct().getUnit().getId();
        }
        // If no unit can be resolved, raise a clear error
        throw new IllegalStateException(
            "Unit is required to log transfer transaction for product " +
            (item.getProduct() != null ? item.getProduct().getId() : "null")
        );
    }
}
