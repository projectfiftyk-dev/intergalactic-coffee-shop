package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;

public record ProductResponse(
        Long id,
        Long version,
        String name,
        ProductStatus productStatus
) {
}
