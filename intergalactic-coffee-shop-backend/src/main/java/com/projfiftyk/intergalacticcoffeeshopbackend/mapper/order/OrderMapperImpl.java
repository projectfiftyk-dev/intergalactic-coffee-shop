package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderItem;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderItemResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public List<OrderResponse> map(List<Order> orders) {
        return orders.stream()
                .map(this::map)
                .toList();
    }

    @Override
    public OrderResponse map(Order order) {
        List<OrderItemResponse> orderItems = order.getOrderItems()
                .stream()
                .map(this::mapOrderItem)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getCreatedAt(),
                orderItems
        );
    }

    private OrderItemResponse mapOrderItem(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getOrderId(),
                orderItem.getProductId(),
                orderItem.getProductName()
        );
    }
}