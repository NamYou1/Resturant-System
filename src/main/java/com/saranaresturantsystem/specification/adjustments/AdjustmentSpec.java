package com.saranaresturantsystem.specification.adjustments;

import com.saranaresturantsystem.entities.Adjustment;
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

            // យកតែទិន្នន័យដែលមិនទាន់លុប (delete_flag = 0)
            predicates.add(cb.equal(root.get("deleteFlag"), 0));

            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
