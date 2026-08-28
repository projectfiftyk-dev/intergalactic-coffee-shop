package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;

public record ProductStatusUpdateRequest(
        ProductStatus productStatus
) {
}
