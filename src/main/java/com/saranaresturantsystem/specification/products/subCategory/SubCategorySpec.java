package com.saranaresturantsystem.specification.products.subCategory;

import com.saranaresturantsystem.entities.product.SubCategory;
import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SubCategorySpec {
    public static Specification<SubCategory> filterBy(SubCategoryFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Always filter by ACTIVE status (soft delete)
            predicates.add(cb.equal(root.get("status"), StatusType.ACTIVE));

            if (filter.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), filter.getCategoryId()));
            }
            if (filter.getSection() != null && !filter.getSection().isEmpty()) {
                predicates.add(cb.like(root.get("section"), "%" + filter.getSection() + "%"));
            }
            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                predicates.add(cb.like(root.get("status"), "%" + filter.getStatus() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
