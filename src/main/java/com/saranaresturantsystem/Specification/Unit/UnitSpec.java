package com.saranaresturantsystem.Specification.Unit;
import com.saranaresturantsystem.Enities.Unit;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
public class UnitSpec {
    public static Specification<Unit> filterBy(UnitFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getCode() != null && !filter.getCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + filter.getCode().toLowerCase() + "%"));
            }
            if (filter.getBaseUnit() != null) {
                predicates.add(cb.equal(root.get("baseUnit"), filter.getBaseUnit()));
            }
            if (filter.getDeleteFlag() != null) {
                predicates.add(cb.equal(root.get("deleteFlag"), filter.getDeleteFlag()));
            } else {
                predicates.add(cb.equal(root.get("deleteFlag"), 0));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}