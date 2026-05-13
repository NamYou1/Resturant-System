package com.saranaresturantsystem.specification.purchases.Expenses;

import com.saranaresturantsystem.entities.Expenses;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ExpensesSpec {

    public static Specification<Expenses> filterBy(ExpensesFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Name
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filter.getName().toLowerCase() + "%"
                        )
                );
            }

            // Reference
            if (filter.getReference() != null && !filter.getReference().isEmpty()) {
                predicates.add(
                        cb.equal(root.get("reference"), filter.getReference())
                );
            }

            // Date
            if (filter.getDate() != null) {
                predicates.add(
                        cb.equal(root.get("date"), filter.getDate())
                );
            }

            // Amount
            if (filter.getAmount() != null) {
                predicates.add(
                        cb.equal(root.get("amount"), filter.getAmount())
                );
            }

            // Note
            if (filter.getNote() != null && !filter.getNote().isEmpty()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("note")),
                                "%" + filter.getNote().toLowerCase() + "%"
                        )
                );
            }

            // Attachment
            if (filter.getAttachment() != null && !filter.getAttachment().isEmpty()) {
                predicates.add(
                        cb.equal(root.get("attachment"), filter.getAttachment())
                );
            }

            // Store ID
            if (filter.getStoreid() != null) {
                predicates.add(
                        cb.equal(root.get("storeid"), filter.getStoreid())
                );
            }

            // Bank ID
            if (filter.getBankid() != null) {
                predicates.add(
                        cb.equal(root.get("bankid"), filter.getBankid())
                );
            }

            // Expenses Type ID
            if (filter.getExpensestypeid() != null) {
                predicates.add(
                        cb.equal(root.get("expensestypeid"), filter.getExpensestypeid())
                );
            }

            // Created By
            if (filter.getCreateBy() != null) {
                predicates.add(
                        cb.equal(root.get("createBy"), filter.getCreateBy())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}