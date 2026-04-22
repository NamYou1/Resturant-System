package com.saranaresturantsystem.specification.purchase;

import com.saranaresturantsystem.entities.Purchase;
package com.saranaresturantsystem.Specification.Purchase;

import com.saranaresturantsystem.Enities.Purchase;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseSpec {
    public static Specification<Purchase> filter(PurchaseFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getReference() != null && !filter.getReference().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("reference")), "%" + filter.getReference().toLowerCase() + "%"));
            }

            if (filter.getSupplierId() != null) {
                predicates.add(cb.equal(root.get("supplier").get("id"), filter.getSupplierId()));
            }

            if (filter.getStoreId() != null) {
                predicates.add(cb.equal(root.get("store").get("id"), filter.getStoreId()));
            }

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("purchasesStatus"), filter.getStatus()));
            }

            // យកតែទិន្នន័យដែលមិនទាន់លុប (delete_flag = 0)
            predicates.add(cb.equal(root.get("deleteFlag"), 0));

            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}