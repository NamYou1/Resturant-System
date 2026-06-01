package com.saranaresturantsystem.services.impl.product;

import com.saranaresturantsystem.common.CloudinaryService;
import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.product.CategoryRequest;
import com.saranaresturantsystem.dto.response.product.CategoryResponse;
import com.saranaresturantsystem.entities.product.Category;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.product.CategoryMapper;
import com.saranaresturantsystem.repositories.product.CategoryRepository;
import com.saranaresturantsystem.services.CategoryService;
import com.saranaresturantsystem.specification.products.category.CategoryFilter;
import com.saranaresturantsystem.specification.products.category.CategorySpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;
    private final CategoryMapper categoryMapper;
    private final CloudinaryService cloudinaryService;
    private final UniqueChecker uniqueChecker;

    // ─────────────────────────────────────────────
    // GET ALL — with filter + pagination
    // ─────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getListCategory(Map<String, String> params) {
        CategoryFilter filter = objectMapper.convertValue(params, CategoryFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<Category> spec = CategorySpec.filterBy(filter);

        return categoryRepository.findAll(spec, pageable)
                .map(categoryMapper::toCategoryResponse);
    }

    // ─────────────────────────────────────────────
    // GET BY ID (internal — returns entity)
    // ─────────────────────────────────────────────
    @Transactional(readOnly = true)
    public Category findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        if (category.getStatus() == StatusType.INACTIVE) {
            throw new ResourceNotFoundException("Category", id);
        }

        return category;
    }

    // ─────────────────────────────────────────────
    // GET BY ID (public — returns DTO)
    // ─────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        return categoryMapper.toCategoryResponse(findCategoryById(id));
    }

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────
    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toCategory(request);
        uniqueChecker.verify(categoryRepository, category, "code", category.getCode());
        uniqueChecker.verify(categoryRepository, category, "name", category.getName());

        if (request.getImagePath() != null && !request.getImagePath().isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(request.getImagePath(), "category");
            category.setImageUrl(imageUrl);
            log.info("Image uploaded for new category [code={}]: {}", category.getCode(), imageUrl);
        } else {
            category.setImageUrl(null);
        }

        Category saved = categoryRepository.save(category);
        log.info("Category created with id={}", saved.getId());
        return categoryMapper.toCategoryResponse(saved);
    }
    // ─────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────
    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategoryById(id);

        if (!category.getCode().equals(request.getCode())) {
            uniqueChecker.verify(categoryRepository, category, "code", request.getCode());
        }
        if (!category.getName().equals(request.getName())) {
            uniqueChecker.verify(categoryRepository, category, "name", request.getName());
        }
        categoryMapper.updateEntityFromRequest(request, category);
        if (request.getImagePath() != null && !request.getImagePath().isEmpty()) {
            if (category.getImageUrl() != null && !category.getImageUrl().isBlank()) {
                try {
                    cloudinaryService.deleteImage(category.getImageUrl());
                    log.info("Old image deleted for category id={}", id);
                } catch (Exception ex) {
                    log.warn("Could not delete old image for category id={}: {}", id, ex.getMessage());
                }
            }
            String newImageUrl = cloudinaryService.uploadImage(request.getImagePath(), "category");
            category.setImageUrl(newImageUrl);
            log.info("New image uploaded for category id={}: {}", id, newImageUrl);
        }
        Category saved = categoryRepository.save(category);
        log.info("Category updated id={}", saved.getId());
        return categoryMapper.toCategoryResponse(saved);
    }
    // ─────────────────────────────────────────────
    // DELETE (soft)
    // ─────────────────────────────────────────────
    // ─────────────────────────────────────────────
// DELETE (hard — removes from DB + Cloudinary)
// ─────────────────────────────────────────────
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = findCategoryById(id);
        // Delete image from Cloudinary first (best-effort)
        if (category.getImageUrl() != null && !category.getImageUrl().isBlank()) {
            try {
                cloudinaryService.deleteImage(category.getImageUrl());
                log.info("Image deleted from Cloudinary for category id={}", id);
            } catch (Exception ex) {
                log.warn("Could not delete image from Cloudinary for category id={}: {}", id, ex.getMessage());
            }
        }

        categoryRepository.delete(category);
        log.info("Category hard-deleted id={}", id);
    }
}