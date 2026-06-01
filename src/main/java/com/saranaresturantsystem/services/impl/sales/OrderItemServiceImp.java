package com.saranaresturantsystem.services.impl.sales;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.sales.OrderItemRequest;
import com.saranaresturantsystem.dto.response.sales.OrderItemResponse;
import com.saranaresturantsystem.entities.sales.OrderItem;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.sales.OrderItemMapper;
import com.saranaresturantsystem.repositories.sales.OrderItemRepository;
import com.saranaresturantsystem.services.OrderItemService;
import com.saranaresturantsystem.specification.sales.OrderItem.OrderItemFilter;
import com.saranaresturantsystem.specification.sales.OrderItem.OrderItemSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
@Service
@RequiredArgsConstructor
public class OrderItemServiceImp implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ObjectMapper objectMapper;
    private final OrderItemMapper orderItemMapper;
    private final UniqueChecker uniqueChecker;

    @Override
    public Page<OrderItemResponse> getAllOrderItem(Map<String, String> params) {
        OrderItemFilter filter = objectMapper.convertValue(params, OrderItemFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<OrderItem> spec = OrderItemSpec.filterBy(filter);
        return orderItemRepository.findAll(spec, pageable)
                .map(orderItemMapper::toOrderItemResponse);
    }

    @Override
    public OrderItem getOrderById(long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem", id));
    }

    @Override
    public OrderItemResponse createOrderItem(OrderItemRequest request) {

        OrderItem orderItem = orderItemMapper.toOrderItem(request);

        uniqueChecker.verify(
                orderItemRepository,
                orderItem,
                "quantity",
                orderItem.getQuantity()
        );

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        return orderItemMapper.toOrderItemResponse(savedOrderItem);
    }

    @Override
    public OrderItemResponse updateOrderItem(Long id, OrderItemRequest request) {
        OrderItem orderItem = getOrderById(id);
        orderItemMapper.updateOrderItemFromResponse(request, orderItem);
        return getOrderItemResponseById(request, orderItem);
    }

    private OrderItemResponse getOrderItemResponseById(OrderItemRequest request, OrderItem orderItem) {
        uniqueChecker.verify(orderItemRepository,orderItem,"quantity",orderItem.getQuantity());
        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toOrderItemResponse(updatedOrderItem);
    }

    @Override
    public OrderItemResponse getOrderItemResponseById(Long id) {
        return orderItemMapper.toOrderItemResponse(getOrderById(id));
    }

    @Override
    public void deleteOrderItem(Long id) {
        OrderItem orderItem=getOrderById(id);

        orderItemRepository.save(orderItem);

    }

    @Override
    public OrderItemResponse findById(Long id) {
        return orderItemMapper.toOrderItemResponse(getOrderById(id));
    }
}
