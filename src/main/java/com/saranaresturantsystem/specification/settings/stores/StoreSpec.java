package com.saranaresturantsystem.specification.settings.stores;

import com.saranaresturantsystem.entities.Store;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StoreSpec {
    public static Specification<Store> filterBy(StoreFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("status"), GeneralStatus.ACTIVE));
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            if (filter.getCode() != null && !filter.getCode().isEmpty()) {
                predicates.add(cb.like(root.get("code"), "%" + filter.getCode() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                predicates.add(cb.like(root.get("email"), "%" + filter.getEmail() + "%"));
            }
            if (filter.getPhone() != null && !filter.getPhone().isEmpty()) {
                predicates.add(cb.like(root.get("phone"), "%" + filter.getName() + "%"));
            }
            if (filter.getCity() != null && !filter.getCity().isEmpty()) {
                predicates.add(cb.like(root.get("city"), "%" + filter.getCity() + "%"));
            }
            if (filter.getState() != null && !filter.getState().isEmpty()) {
                predicates.add(cb.like(root.get("state"), "%" + filter.getState() + "%"));
            }
            if (filter.getCountry() != null && !filter.getCountry().isEmpty()) {
                predicates.add(cb.like(root.get("country"), "%" + filter.getCountry() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
