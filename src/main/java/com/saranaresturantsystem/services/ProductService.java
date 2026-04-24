package com.saranaresturantsystem.Services;

import com.saranaresturantsystem.DTO.Request.ProductRequest;
import com.saranaresturantsystem.DTO.Response.ProductResponse;
import com.saranaresturantsystem.Enities.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ProductService {
    Page<ProductResponse> getAllProducts(Map<String, String> params);
    ProductResponse findById(Long id);
    Product getProductById(Long id);
    ProductResponse createProduct(ProductRequest request, MultipartFile file);
    ProductResponse updateProduct(Long id, ProductRequest request, MultipartFile file);
    void deleteProduct(Long id);
    void updateStock(Long productId, Integer storeId, java.math.BigDecimal quantity);
}