package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.product.ProductRequest;
import com.saranaresturantsystem.dto.response.product.ProductResponse;
import com.saranaresturantsystem.entities.product.Product;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Map;

public interface ProductService {
    Page<ProductResponse> getAllProducts(Map<String, String> params);
    ProductResponse findById(Long id);
    Product getProductById(Long id);
    ProductResponse createProduct(ProductRequest request );
    ProductResponse updateProduct(Long id, ProductRequest request );
    void deleteProduct(Long id);
    void updateStock(Long productId, Long storeId, BigDecimal quantity);
}