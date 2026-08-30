package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request;

public record OrderCreateRequest (
        Long productId,
        int quantity
) {
}
