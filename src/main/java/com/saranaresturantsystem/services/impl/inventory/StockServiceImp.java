package com.saranaresturantsystem.services.impl.inventory;

import com.saranaresturantsystem.dto.response.inventory.StockResponse;
import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.entities.inventory.ProductStoreQty;
import com.saranaresturantsystem.entities.inventory.Store;
import com.saranaresturantsystem.execption.InsufficientStockException;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.inventory.StockMapper;
import com.saranaresturantsystem.repositories.inventory.StockRepository;
import com.saranaresturantsystem.services.ProductService;
import com.saranaresturantsystem.services.StockService;
import com.saranaresturantsystem.services.StoreService;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
@RequiredArgsConstructor
@Service
public class StockServiceImp implements StockService {
    private  final StockRepository stockRepository ;
    private  final ProductService productService ;
    private  final StockMapper stockMapper;
    private  final StoreService storeService ;
//    private  final  userRepository;


    @Override
    public ProductStoreQty findProductAndStoreById(Long productId, Long storeId) {
        Product product = productService.getProductById(productId);
        Store store = storeService.findById(storeId);
        return  stockRepository.findByProductAndStore(product , store).orElseThrow(()-> new  ResourceNotFoundException("Store and Product" , productId ,storeId));
        }

        @Override
    public StockResponse getById(Long id) {
        ProductStoreQty stock = stockRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Stock", id));
        return stockMapper.toResponse(stock);
    }

    @Override
    public StockResponse getByProductAndStore(Product productId, Store storeId) {
        ProductStoreQty stock = findProductAndStoreById(productId.getId(), storeId.getId());
        return  stockMapper.toResponse(stock);
    }



    @Override
    public Page<StockResponse> getAll(Map<String, String> params) {
        Pageable pageable = PageUtil.fromParams(params);
        return stockRepository.findAll(pageable).map(stockMapper::toResponse);
    }

    @Override
    public Page<StockResponse> getByStore(Long storeId, Map<String, String> params) {
        Pageable pageable = PageUtil.fromParams(params);
        return stockRepository.findByStore(storeId, pageable).map(stockMapper::toResponse);

    }

    @Override
    public void reverseStock(Long productId, Long fromStoreId, Long toStoreId, BigDecimal quantity) {
        // RETURN STOCK TO SOURCE
        increaseStock(productId, fromStoreId, quantity, BigDecimal.ZERO);
        // REMOVE STOCK FROM DESTINATION
        decreaseStock(productId, toStoreId, quantity);
    }

    @Override
    public void increaseStock(Long productId, Long storeId , BigDecimal quantity, BigDecimal costPrice) {
        Product product= productService.getProductById(productId);
        Store stores = storeService.findById(storeId);
        ProductStoreQty stock = stockRepository.findByProductAndStore(product, stores)
                .orElseGet(() -> {
                    ProductStoreQty newStock = new ProductStoreQty();
                    newStock.setProduct(product);
                    newStock.setStore(stores);
                    newStock.setQuantity(BigDecimal.ZERO);
                    return newStock;
                });
        stock.setQuantity(stock.getQuantity().add(quantity));
        stockRepository.save(stock);
    }

    @Override
    public void decreaseStock(Long productId, Long storeId, BigDecimal quantity) {
        ProductStoreQty stock = findProductAndStoreById(productId, storeId);
        validateStock(stock, quantity);
        stock.setQuantity(stock.getQuantity().subtract(quantity));
        stockRepository.save(stock);
    }


    @Override
    public void transferStock(long productId, Long fromStore, long toStore, BigDecimal quantity) {
        decreaseStock(productId, fromStore, quantity);
        increaseStock(productId, toStore, quantity, BigDecimal.ZERO);
    }

    // if have adjustment we can use this for detail about the adjustment it's have more adjustmentype
//    @Override
//    public void adjustStock(Long product, Long store, BigDecimal quantity, AdjustmentType type) {
//        if (type == null) {
//            throw new IllegalArgumentException("Adjustment type is required");
//        }
//        if (type == AdjustmentType.INCREASE) {
//            increaseStock(product, store, quantity, BigDecimal.ZERO);
//        } else if (type == AdjustmentType.DECREASE) {
//            decreaseStock(product, store, quantity);
//        }
//    }

    @Override
    public void validateStock(ProductStoreQty stock, BigDecimal quantity) {
        // Check stock exists
        if (stock == null) {
            throw new ResourceNotFoundException("Stock not found");
        }
        // Check quantity null or invalid
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        // Check available stock
        if (stock.getQuantity().compareTo(quantity) < 0) {
            throw new InsufficientStockException(stock.getProduct().getName(), quantity.intValue(), stock.getQuantity().intValue());
        }
    }
}
