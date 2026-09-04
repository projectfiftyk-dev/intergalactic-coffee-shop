package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionStatus;

import java.time.LocalDateTime;
import java.util.List;

public record PromotionListFilterRequest(
    LocalDateTime createdAtFrom,
    LocalDateTime createdAtTo,
    LocalDateTime startDateFrom,
    LocalDateTime startDateTo,
    List<PromotionStatus> statuses
) {
}
