package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;

public record OrderUpdateRequest(
        OrderStatus status
) {
}
