package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionSortField;

public record PromotionListRequest(
        int pageNumber,
        int pageSize,
        PromotionSortField sortField,
        SortDirection direction
) {
}
