package com.saranaresturantsystem.specification.peoples.suppliers;

import com.saranaresturantsystem.entities.Supplier;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SupplerSpec {
    public static Specification<Supplier> filterBy(SupplerFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Always filter by ACTIVE status (soft delete)
            predicates.add(cb.equal(root.get("status"), GeneralStatus.ACTIVE));

            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(cb.upper(root.get("name")), "%" + filter.getName().toUpperCase() + "%"));
            }
            if (filter.getAddressOne() != null && !filter.getAddressOne().isEmpty()) {
                predicates.add(cb.like(root.get("addressOne"), "%" + filter.getAddressOne() + "%"));
            }
            if (filter.getAddressTwo() != null && !filter.getAddressTwo().isEmpty()) {
                predicates.add(cb.like(root.get("addressTwo"), "%" + filter.getAddressTwo() + "%"));
            }
            if (filter.getPhone() != null && !filter.getPhone().isEmpty()) {
                predicates.add(cb.like(root.get("phone"), "%" + filter.getPhone() + "%"));
            }
            if (filter.getAddress() != null && !filter.getAddress().isEmpty()) {
                predicates.add(cb.like(root.get("address"), "%" + filter.getAddressOne() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                predicates.add(cb.like(root.get("email"), "%" + filter.getAddressOne() + "%"));
            }
            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                predicates.add(cb.like(root.get("status"), "%" + filter.getStatus() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
