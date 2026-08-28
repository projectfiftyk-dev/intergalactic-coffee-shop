package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(
        @NotBlank
        @Size(min = 5, max = 50)
        String name
) {

}