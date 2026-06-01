package com.saranaresturantsystem.repositories.sales;

import com.saranaresturantsystem.entities.sales.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>,JpaSpecificationExecutor<OrderItem> {
}
