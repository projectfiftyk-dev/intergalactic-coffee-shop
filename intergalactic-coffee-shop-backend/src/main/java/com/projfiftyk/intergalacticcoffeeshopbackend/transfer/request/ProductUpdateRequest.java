package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest (
        @NotBlank
        @Size(min = 5, max = 50)
        String name) {
}
