package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import jakarta.validation.constraints.NotBlank;

public record ProductStatusUpdateRequest(
        @NotBlank
        ProductStatus productStatus
) {
}
