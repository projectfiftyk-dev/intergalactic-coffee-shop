package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderResponse;

import java.util.List;

public class OrderMapperImpl implements OrderMapper {

    @Override
    public List<OrderResponse> map(List<Order> orders) {
        return orders.stream()
                .map(order -> {
                    return new OrderResponse(
                            order.getId(),
                            order.getProductName(),
                            order.getStatus(),
                            order.getCreatedAt()
                    );
                })
                .toList();
    }

    @Override
    public OrderResponse map(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductName(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
