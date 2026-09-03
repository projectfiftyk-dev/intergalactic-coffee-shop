package com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;

public record ProductListRequest(
        int pageNumber,
        int pageSize,
        ProductSortField sortField,
        SortDirection direction
) {
}
