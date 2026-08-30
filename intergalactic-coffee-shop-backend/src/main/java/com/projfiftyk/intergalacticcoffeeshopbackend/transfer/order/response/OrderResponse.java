package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        String productName,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
