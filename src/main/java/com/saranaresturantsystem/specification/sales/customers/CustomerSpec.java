package com.saranaresturantsystem.specification.sales.customers;

import com.saranaresturantsystem.entities.sales.Customer;
import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerSpec {
        public static Specification<Customer> filterBy(CustomerFilter filter) {
            return (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                // Always filter by ACTIVE status (soft delete)
                predicates.add(cb.equal(root.get("status"), StatusType.ACTIVE));

                if (filter.getName() != null && !filter.getName().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
                }

                if (filter.getStoreId() != null) {
                    predicates.add(cb.equal(root.get("store").get("id"), filter.getStoreId()));
                }

                if (filter.getCustomerGroupId() != null) {
                    predicates.add(cb.equal(root.get("customerGroup").get("id"), filter.getCustomerGroupId()));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            };
        }
}
