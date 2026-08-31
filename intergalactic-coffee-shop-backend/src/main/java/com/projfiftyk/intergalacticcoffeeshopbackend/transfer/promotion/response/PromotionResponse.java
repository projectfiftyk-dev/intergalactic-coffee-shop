package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionRewardType;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PromotionResponse(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime startDate,
        LocalDateTime endDate,
        PromotionStatus status,
        PromotionType promotionType,
        Integer occurrences,
        BigDecimal minimumValue,
        List<Long> productIds,
        List<Long> requiredProducts,
        PromotionRewardType rewardType,
        BigDecimal rewardValue
) {
}