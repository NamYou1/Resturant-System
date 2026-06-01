package com.saranaresturantsystem.specification.inventory.ExpensesType;

import com.saranaresturantsystem.entities.finance.ExpensesType;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExpensesTypeSpec {
    public static Specification<ExpensesType> filterBy(ExpensesTypeFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
                predicates.add(cb.like(cb.upper(root.get("description")), "%" + filter.getDescription().toUpperCase() + "%"));
            }
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            if(filter.getStatus() != null){
                predicates.add(cb.equal(root.get("active"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
