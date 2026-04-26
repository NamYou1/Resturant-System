package com.saranaresturantsystem.specification.sales;

import com.saranaresturantsystem.entities.Sale;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SaleSpec {
    public static Specification<Sale> filter(SaleFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter តាមឈ្មោះអតិថិជន (Case-insensitive)
            if (filter.getCustomerName() != null && !filter.getCustomerName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("customerName")),
                        "%" + filter.getCustomerName().toLowerCase() + "%"));
            }

            // Filter តាម Store ID
            if (filter.getStoreId() != null) {
                predicates.add(cb.equal(root.get("storeId"), filter.getStoreId()));
            }

            // Filter តាម Status (ប្រសិនបើអ្នកចង់បន្ថែម)
            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                predicates.add(cb.equal(root.get("saleStatus"), filter.getStatus()));
            }

            // យកតែទិន្នន័យដែលមិនទាន់លុប (delete_flag = 0)
            predicates.add(cb.equal(root.get("deleteFlag"), 0));

            // តម្រៀបតាម ID ពីធំមកតូច (Newest first)
            query.orderBy(cb.desc(root.get("id")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}