package com.saranaresturantsystem.specification.settings.currency;

import com.saranaresturantsystem.entities.Currency;
import com.saranaresturantsystem.entities.Group;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.specification.settings.groups.GroupFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CurrencySpec {
    public static Specification<Currency> filterBy(CurrencyFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
//            // Always filter by ACTIVE status (soft delete)
//            predicates.add(cb.equal(root.get("status"), GeneralStatus.ACTIVE));

            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            if (filter.getCode() != null && !filter.getCode().isEmpty()) {
                predicates.add(cb.like(root.get("code"), "%" + filter.getCode() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
