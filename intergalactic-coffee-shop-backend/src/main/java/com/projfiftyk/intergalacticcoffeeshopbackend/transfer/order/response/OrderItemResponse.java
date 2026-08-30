package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response;

public record OrderItemResponse(
        Long id,
        Long orderId,
        Long productId,
        String productName
) {
}
