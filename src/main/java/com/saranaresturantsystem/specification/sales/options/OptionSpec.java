package com.saranaresturantsystem.specification.sales.options;


import com.saranaresturantsystem.entities.product.Options;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OptionSpec {

    public static Specification<Options> filterBy(OptionFilter filter) {

        return (root, query, criteriaBuilder) -> {

            List<java.util.function.Predicate> predicates = new ArrayList<>();

            // NAME filter (search like)
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(
                        (Predicate) criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + filter.getName().toLowerCase() + "%"
                        )
                );
            }

            // GROUP ID filter
            if (filter.getGroupId() != null) {
                predicates.add(
                        (Predicate) criteriaBuilder.equal(
                                root.get("group").get("id"),
                                filter.getGroupId()
                        )
                );
            }

            // PRICE filter (FIXED - assuming price is field, not relation)
            if (filter.getPrice() != null) {
                predicates.add(
                        (Predicate) criteriaBuilder.equal(
                                root.get("price"),
                                filter.getPrice()
                        )
                );
            }

            // DELETE FLAG (FIXED - assuming boolean/int field)
            if (filter.getDeleteFlag() != null) {
                predicates.add(
                        (Predicate) criteriaBuilder.equal(
                                root.get("deleteFlag"),
                                filter.getDeleteFlag()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}