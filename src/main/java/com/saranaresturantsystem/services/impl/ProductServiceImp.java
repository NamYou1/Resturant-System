package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.ProductRequest;
import com.saranaresturantsystem.dto.response.ProductResponse;
import com.saranaresturantsystem.entities.Product;
import com.saranaresturantsystem.entities.ProductStoreQty;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.mappers.ProductMapper;
import com.saranaresturantsystem.repositories.ProductRepository;
import com.saranaresturantsystem.repositories.ProductStoreQtyRepository;
import com.saranaresturantsystem.services.ProductService;
import com.saranaresturantsystem.specification.products.ProductFilter;
import com.saranaresturantsystem.specification.products.ProductSpec;
import com.saranaresturantsystem.utils.GloblePagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductStoreQtyRepository productStoreQtyRepository;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper ;
    private final UniqueChecker uniqueChecker;
    private final String UPLOAD_DIR = "uploads/products/";
    @Override
    public Page<ProductResponse> getAllProducts(Map<String, String> params) {
        ProductFilter filter = objectMapper.convertValue(params, ProductFilter.class);

        int pageLimit = params.containsKey(GloblePagination.DEFAULT_PAGE_LIMIT)
                ? Integer.parseInt(params.get(GloblePagination.DEFAULT_PAGE_LIMIT))
                : GloblePagination.DEFAULT_PAGE_LIMIT;
        int pageNumber = params.containsKey(GloblePagination.DEFAULT_PAGE_NUMBER)
                ? Integer.parseInt(params.get(GloblePagination.DEFAULT_PAGE_NUMBER))
                : 0;
        Pageable pageable = PageRequest.of(pageNumber, pageLimit);
        Specification<Product> spec = ProductSpec.filterBy(filter);
        return productRepository.findAll(spec, pageable)
                .map(productMapper::toProductResponse);
    }
    @Override
    public ProductResponse findById(Long id) {
        return productMapper.toProductResponse(getProductById(id));
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExecption("Product", id));
    }
    @Override
    public ProductResponse createProduct(ProductRequest request, MultipartFile file) {
        Product product = productMapper.toProduct(request);
        uniqueChecker.verify(productRepository, product, "code", product.getCode());
        if (file != null && !file.isEmpty()) {
            product.setImage(saveImage(file));
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request, MultipartFile file) {
        Product product = getProductById(id);
        productMapper.updateProductFromRequest(request, product);
        uniqueChecker.verify(productRepository, product, "code", product.getCode());
        if (file != null && !file.isEmpty()) {
            product.setImage(saveImage(file));
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setShowFlag(1);product.setStatus(GeneralStatus.INACTIVE);
        productRepository.save(product);
    }

    @Override
    public void updateStock(Long productId, Integer storeId, BigDecimal quantity) {
        Product product = getProductById(productId);
        ProductStoreQty stock = productStoreQtyRepository
                .findByProductIdAndStoreId(productId, storeId)
                .orElseGet(() -> {
                    ProductStoreQty newStock = new ProductStoreQty();
                    newStock.setProduct(product);
                    newStock.setStoreId(storeId);
                    newStock.setQuantity(BigDecimal.ZERO);
                    newStock.setPrice(product.getPrice());
                    return newStock;
                });
        stock.setQuantity(stock.getQuantity().add(quantity));
        productStoreQtyRepository.save(stock);
    }
    private String saveImage(MultipartFile file) {
        try {
            Path root = Paths.get(UPLOAD_DIR);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), root.resolve(fileName));
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
}