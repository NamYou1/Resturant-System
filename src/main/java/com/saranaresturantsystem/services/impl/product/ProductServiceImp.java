package com.saranaresturantsystem.services.impl.product;

import com.saranaresturantsystem.common.CloudinaryService;
import com.saranaresturantsystem.common.FileStorageService;
import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.product.ProductRequest;
import com.saranaresturantsystem.dto.response.product.ProductResponse;
import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.entities.inventory.ProductStoreQty;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.product.ProductMapper;
import com.saranaresturantsystem.repositories.product.ProductRepository;
import com.saranaresturantsystem.repositories.inventory.ProductStoreQtyRepository;
import com.saranaresturantsystem.repositories.inventory.StoreRepository;
import com.saranaresturantsystem.services.ProductService;
import com.saranaresturantsystem.specification.products.product.ProductFilter;
import com.saranaresturantsystem.specification.products.product.ProductSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductStoreQtyRepository productStoreQtyRepository;
    private final StoreRepository storeRepository;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;
    private final UniqueChecker uniqueChecker;
    private final CloudinaryService cloudinaryService;
    private  final FileStorageService fileStorageService ;

    // ─────────────────────────────────────────────
    // GET ALL — with filtering + pagination
    // ─────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Map<String, String> params) {
        ProductFilter filter = objectMapper.convertValue(params, ProductFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<Product> spec = ProductSpec.filterBy(filter);

        return productRepository.findAll(spec, pageable)
                .map(productMapper::toProductResponse);
    }

    // ─────────────────────────────────────────────
    // GET BY ID
    // ─────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return productMapper.toProductResponse(getProductById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        if (product.getStatus() == StatusType.INACTIVE) {
            throw new ResourceNotFoundException("Product", id);
        }
        return product;
    }

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────
    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request ) {
        // Check unique code before anything else
        Product product = productMapper.toProduct(request);
        uniqueChecker.verify(productRepository, product, "code", request.getCode());

        // Upload image to Cloudinary if provided
//        if (request.getImage() != null && !request.getImage().isEmpty()) {
//            String imageUrl = fileStorageService.uploadFile(file, "products");
//            product.setImage(imageUrl);
//            log.info("Image uploaded for new product [code={}]: {}", product.getCode(), imageUrl);
//        }

        Product saved = productRepository.save(product);
        log.info("Product created with id={}", saved.getId());
        return productMapper.toProductResponse(saved);
    }

    // ─────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────
    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);
        if (!product.getCode().equals(request.getCode())) {
            uniqueChecker.verify(productRepository, product, "code", request.getCode());
        }

        productMapper.updateProductFromRequest(request, product);
//        if (request.getImage() != null && !request.getImage().isEmpty()) {
//            if (product.getImage() != null && !product.getImage().isBlank()) {
//                try {
//                    cloudinaryService.deleteImage(product.getImage());
//                    log.info("Old image deleted for product id={}", id);
//                } catch (Exception ex) {
//                    log.warn("Could not delete old image for product id={}: {}", id, ex.getMessage());
//                }
//            }
//            String newImageUrl = cloudinaryService.uploadImage(request.getImage(), "products");
//            product.setImage(newImageUrl);
//            log.info("New image uploaded for product id={}: {}", id, newImageUrl);
//        }

        Product saved = productRepository.save(product);
        log.info("Product updated id={}", saved.getId());
        return productMapper.toProductResponse(saved);
    }

    // ─────────────────────────────────────────────
    // DELETE (soft)
    // ─────────────────────────────────────────────
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setStatus(StatusType.INACTIVE);
        productRepository.save(product);
        log.info("Product soft-deleted id={}", id);
    }

    // ─────────────────────────────────────────────
    // UPDATE STOCK
    // ─────────────────────────────────────────────
    @Override
    @Transactional
    public void updateStock(Long productId, Long storeId, BigDecimal quantity) {
        Product product = getProductById(productId);

        ProductStoreQty stock = productStoreQtyRepository
                .findByProductIdAndStoreId(productId, storeId)
                .orElseGet(() -> {
                    ProductStoreQty newStock = new ProductStoreQty();
                    newStock.setProduct(product);
                    newStock.setStore(storeRepository.findById(storeId)
                            .orElseThrow(() -> new ResourceNotFoundException("Store", storeId)));
                    newStock.setQuantity(BigDecimal.ZERO);
                    newStock.setPrice(product.getSalePrice());
                    return newStock;
                });

        stock.setQuantity(stock.getQuantity().add(quantity));
        productStoreQtyRepository.save(stock);
        log.info("Stock updated for productId={} storeId={} qty={}", productId, storeId, quantity);
    }
}