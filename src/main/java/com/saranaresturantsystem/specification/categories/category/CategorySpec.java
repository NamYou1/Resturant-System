package com.saranaresturantsystem.specification.categories.category;

import com.saranaresturantsystem.entities.Category;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategorySpec {
    public static Specification<Category> filterBy(CategoryFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Always filter by ACTIVE status (soft delete)
            predicates.add(cb.equal(root.get("status"), GeneralStatus.ACTIVE));

            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }
            if (filter.getCode() != null && !filter.getCode().isEmpty()) {
                predicates.add(cb.like(cb.upper(root.get("code")), "%" + filter.getCode().toUpperCase() + "%"));
            }
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}