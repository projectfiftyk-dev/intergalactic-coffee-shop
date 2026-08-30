package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemResponse> orderItems
) {
}