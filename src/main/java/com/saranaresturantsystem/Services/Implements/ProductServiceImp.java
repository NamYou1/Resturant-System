package com.saranaresturantsystem.Services.Implements;

import com.saranaresturantsystem.Common.UniqueChecker;
import com.saranaresturantsystem.DTO.Request.ProductRequest;
import com.saranaresturantsystem.DTO.Response.ProductResponse;
import com.saranaresturantsystem.Enities.Product;
import com.saranaresturantsystem.Enities.ProductStoreQty;
import com.saranaresturantsystem.Execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.Mappers.ProductMapper;
import com.saranaresturantsystem.Repositories.ProductRepository;
import com.saranaresturantsystem.Repositories.ProductStoreQtyRepository;
import com.saranaresturantsystem.Services.ProductService;
import com.saranaresturantsystem.Specification.Product.ProductFilter;
import com.saranaresturantsystem.Specification.Product.ProductSpec;
import com.saranaresturantsystem.Utils.GloblePagination;
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
        product.setShowFlag(1);
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