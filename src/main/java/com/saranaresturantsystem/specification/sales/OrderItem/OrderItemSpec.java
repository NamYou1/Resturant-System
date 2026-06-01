package com.saranaresturantsystem.specification.sales.OrderItem;

import com.saranaresturantsystem.entities.sales.OrderItem;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OrderItemSpec{
    public static Specification<OrderItem>filterBy(OrderItemFilter filter){
        return (root, query, criteriaBuilder) -> {
            List<Predicate>predicates = new ArrayList<>();
            if(filter.getOrderId() != null){
                predicates.add((Predicate) criteriaBuilder.like(criteriaBuilder.lower(root.get("orderId")),"%"));
            }
            if(filter.getUserId() != null){
                predicates.add((Predicate) criteriaBuilder.like(criteriaBuilder.lower(root.get("itemId")),"%"));
            }

            if(filter.getQuantity() != null){
                predicates.add((Predicate) criteriaBuilder.like(criteriaBuilder.lower(root.get("quantity")),"%"+filter.getQuantity().toBigInteger().getLowestSetBit()+"%"));
            }
            if(filter.getDate() != null){
                predicates.add((Predicate) criteriaBuilder.like(criteriaBuilder.lower(root.get("date")),"%"));
            }
            if(filter.getTablesId() != null){
                predicates.add((Predicate) criteriaBuilder.like(criteriaBuilder.lower(root.get("tablesId")),"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
