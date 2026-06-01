package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.response.inventory.StockResponse;
import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.entities.inventory.ProductStoreQty;
import com.saranaresturantsystem.entities.inventory.Store;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Map;

public interface StockService {
    ProductStoreQty findProductAndStoreById(Long productId  , Long storeId );
    StockResponse getById(Long id);
    StockResponse getByProductAndStore(Product productId, Store storeId);
    Page<StockResponse> getAll(Map<String, String> params);
    Page<StockResponse> getByStore(Long storeId, Map<String,String> params);
    void reverseStock(Long productId, Long fromStoreId, Long toStoreId, BigDecimal quantity);
    // STOCK IN
    void increaseStock(Long  productId, Long storeId, BigDecimal quantity, BigDecimal costPrice);
    // STOCK OUT
    void decreaseStock(Long productId, Long storeid, BigDecimal quantity);
    // TRANSFER
    void transferStock(long productId, Long fromStore, long toStore, BigDecimal quantity);
    // ADJUSTMENT
//    void adjustStock(Long product, Long store, BigDecimal quantity, AdjustmentType type);
    // VALIDATION
    void validateStock(ProductStoreQty stock, BigDecimal quantity);
}
