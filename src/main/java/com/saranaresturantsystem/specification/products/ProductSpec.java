package com.saranaresturantsystem.specification.products;

import com.saranaresturantsystem.entities.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpec {
    public static Specification<Product> filterBy(ProductFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by Name (Partial Match)
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }

            // Filter by Code (Exact Match)
            if (filter.getCode() != null && !filter.getCode().isEmpty()) {
                predicates.add(cb.equal(root.get("code"), filter.getCode()));
            }

            // Filter by Category Relationship
            if (filter.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), filter.getCategoryId()));
            }

            // Filter by SubCategory (Section) Relationship
            if (filter.getSectionId() != null) {
                predicates.add(cb.equal(root.get("section").get("id"), filter.getSectionId()));
            }

            // Price Range Filtering
            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
            }

            // Visibility Flag
            if (filter.getShowFlag() != null) {
                predicates.add(cb.equal(root.get("showFlag"), filter.getShowFlag()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}