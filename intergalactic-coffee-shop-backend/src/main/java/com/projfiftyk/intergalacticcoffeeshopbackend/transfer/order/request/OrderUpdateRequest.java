package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderUpdateRequest(
        @NotNull
        OrderStatus status
) {
}
