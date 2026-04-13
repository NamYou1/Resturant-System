package com.saranaresturantsystem.specification.settings.groups;

import com.saranaresturantsystem.entities.Group;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class GroupSpec {
    public static Specification<Group> filterBy(GroupFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Always filter by ACTIVE status (soft delete)
            predicates.add(cb.equal(root.get("status"), GeneralStatus.ACTIVE));

            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
