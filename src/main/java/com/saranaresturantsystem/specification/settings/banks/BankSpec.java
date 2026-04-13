package com.saranaresturantsystem.specification.settings.banks;

import com.saranaresturantsystem.entities.Bank;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class BankSpec {
    public static Specification<Bank> filterBy(BankFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getNumber() != null && !filter.getNumber().isEmpty()) {
                predicates.add(cb.like(cb.upper(root.get("number")), "%" + filter.getNumber().toUpperCase() + "%"));
            }
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
