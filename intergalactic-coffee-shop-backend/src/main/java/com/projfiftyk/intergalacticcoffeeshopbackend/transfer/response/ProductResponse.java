package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.response;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;

public record ProductResponse(
        Long id,
        String name,
        ProductStatus productStatus
) {
}
