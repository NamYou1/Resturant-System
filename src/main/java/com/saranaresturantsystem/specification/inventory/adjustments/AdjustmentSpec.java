package com.saranaresturantsystem.specification.inventory.adjustments;

import com.saranaresturantsystem.entities.inventory.Adjustment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AdjustmentSpec {
    public static Specification<Adjustment> filter(AdjustmentFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getReference() != null && !filter.getReference().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("referenceNo")), "%" + filter.getReference().toLowerCase() + "%"));
            }

            if (filter.getStoreId() != null) {
                predicates.add(cb.equal(root.get("store").get("id"), filter.getStoreId()));
            }

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }



            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
