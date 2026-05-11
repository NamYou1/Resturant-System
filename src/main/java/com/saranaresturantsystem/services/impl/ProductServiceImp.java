package com.saranaresturantsystem.services.impl;
import com.saranaresturantsystem.common.FileHandler;
import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.ProductRequest;
import com.saranaresturantsystem.dto.response.ProductResponse;
import com.saranaresturantsystem.entities.Product;
import com.saranaresturantsystem.entities.ProductStoreQty;
import com.saranaresturantsystem.entities.status.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.services.ProductService;
import com.saranaresturantsystem.specification.products.ProductFilter;
import com.saranaresturantsystem.specification.products.ProductSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.saranaresturantsystem.mappers.ProductMapper;
import com.saranaresturantsystem.repositories.ProductRepository;
import com.saranaresturantsystem.repositories.ProductStoreQtyRepository;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductStoreQtyRepository productStoreQtyRepository;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper ;
    private final UniqueChecker uniqueChecker;
    private  final FileHandler fileHandler ;
//    private final String UPLOAD_DIR = "uploads/products/";/**/
    @Override
    public Page<ProductResponse> getAllProducts(Map<String, String> params) {
        ProductFilter filter = objectMapper.convertValue(params, ProductFilter.class);

        Pageable pageable = PageUtil.fromParams(params);

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
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        return getProductResponse(request, product);
    }
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);
        productMapper.updateProductFromRequest(request, product);
        return getProductResponse(request, product);
    }

    //  this function is reusable for create and update it's useful because it handles the unique check and image upload in one place
    private ProductResponse getProductResponse(ProductRequest request, Product product) {
        uniqueChecker.verify(productRepository, product, "code", product.getCode());
//        if (request.getImage() != null && !request.getImage().isEmpty()) {
//            product.setImage(fileHandler.uploadImage(request.getImage(), "products"));
//        }
        // upload image
        if (request.getImage() != null &&
                !request.getImage().isEmpty()) {

            String imageUrl = fileHandler.uploadImage(
                    request.getImage(),
                    "products"
            );

            product.setImage(imageUrl);
        }
        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setStatus(StatusType.ACTIVE);
        productRepository.save(product);
    }

    @Override
    public void updateStock(Long productId, Long
            storeId, BigDecimal quantity) {
        Product product = getProductById(productId);
        ProductStoreQty stock = productStoreQtyRepository
                .findByProductIdAndStoreId(productId, storeId)
                .orElseGet(() -> {
                    ProductStoreQty newStock = new ProductStoreQty();
                    newStock.setProduct(product);
                    newStock.setStoreId(storeId);
                    newStock.setQuantity(BigDecimal.ZERO);
                    newStock.setPrice(product.getSalePrice());
                    return newStock;
                });
        stock.setQuantity(stock.getQuantity().add(quantity));
        productStoreQtyRepository.save(stock);
    }
//    private String saveImage(MultipartFile file) {
//        try {
//            Path root = Paths.get(UPLOAD_DIR);
//            if (!Files.exists(root)) {
//                Files.createDirectories(root);
//            }
//            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//            Files.copy(file.getInputStream(), root.resolve(fileName));
//            return fileName;
//        } catch (IOException e) {
//            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//        }
//    }
}