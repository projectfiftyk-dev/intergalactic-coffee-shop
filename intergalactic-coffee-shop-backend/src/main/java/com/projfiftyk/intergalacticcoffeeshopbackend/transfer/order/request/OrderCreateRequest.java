package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record OrderCreateRequest (
        @NotNull
        Long productId,

        @NotNull
        int quantity
) {
}
