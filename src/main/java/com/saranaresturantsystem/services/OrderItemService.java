package com.saranaresturantsystem.services;
import com.saranaresturantsystem.dto.request.sales.OrderItemRequest;
import com.saranaresturantsystem.dto.response.sales.OrderItemResponse;
import com.saranaresturantsystem.entities.sales.OrderItem;
import org.springframework.data.domain.Page;
import java.util.Map;

public interface OrderItemService {
    Page<OrderItemResponse>getAllOrderItem(Map<String,String>params);
    OrderItem getOrderById(long id);
    OrderItemResponse createOrderItem(OrderItemRequest orderItemRequest);
    OrderItemResponse updateOrderItem(Long id,OrderItemRequest orderItemRequest);
    OrderItemResponse getOrderItemResponseById(Long id);
    void deleteOrderItem(Long id);


    OrderItemResponse findById(Long id);
}
