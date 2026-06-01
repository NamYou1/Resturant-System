package com.saranaresturantsystem.specification.sales.tables;

import com.saranaresturantsystem.entities.sales.Tables;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TableSpec {
    public static Specification<Tables> filterBy(TableFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            if (filter.getOrderNumber() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(root.get("orderNumber"), "%" + filter.getName() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
