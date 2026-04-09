package com.saranaresturantsystem.Specification.Seller;

import com.saranaresturantsystem.Enities.Seller;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class SellerSpec {
    public static Specification<Seller> filterBy(SellerFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null) {
                predicates.add(cb.like(cb.upper(root.get("name")), "%" + filter.getName().toUpperCase() + "%"));
            }
            if (filter.getPhone() != null) {
                predicates.add(cb.like(root.get("phone"), "%" + filter.getPhone() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}