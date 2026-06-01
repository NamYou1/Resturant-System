package com.saranaresturantsystem.specification.sales.groups;

import com.saranaresturantsystem.entities.sales.Group;
import com.saranaresturantsystem.enums.StatusType;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class GroupSpec {
    public static Specification<Group> filterBy(GroupFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Always filter by ACTIVE status (soft delete)
            predicates.add(cb.equal(root.get("status"), StatusType.ACTIVE));

            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
