package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionRewardType;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PromotionUpdateRequest(
        LocalDateTime startDate,
        LocalDateTime endDate,
        PromotionType promotionType,
        Integer occurrences,
        BigDecimal minimumValue,
        List<Long> productIds,
        List<Long> requiredProducts,
        PromotionRewardType rewardType,
        BigDecimal rewardValue
) {
}