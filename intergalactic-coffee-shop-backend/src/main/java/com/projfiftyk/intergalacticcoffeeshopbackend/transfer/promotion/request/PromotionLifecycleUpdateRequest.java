package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionStatus;

public record PromotionLifecycleUpdateRequest(
        PromotionStatus status
) {
}
