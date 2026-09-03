package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductStatusUpdateRequest(
        @NotNull
        ProductStatus productStatus
) {
}
