package com.saranaresturantsystem.Specification.Category;

import com.saranaresturantsystem.Enities.Category;
import com.saranaresturantsystem.Enities.SubCategory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SubCategorySpec {
    public static Specification<SubCategory> filterBy(SubCategoryFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), filter.getCategoryId()));
            }
//            if (filter.getId() != 0) {
//                predicates.add(cb.equal(root.get("id"), filter.getId()));
//            }
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
