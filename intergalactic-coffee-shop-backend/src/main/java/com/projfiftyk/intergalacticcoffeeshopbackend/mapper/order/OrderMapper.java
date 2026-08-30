package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderResponse;

import java.util.List;

public interface OrderMapper {
    List<OrderResponse> map(List<Order> orders);

    OrderResponse map(Order order);
}
