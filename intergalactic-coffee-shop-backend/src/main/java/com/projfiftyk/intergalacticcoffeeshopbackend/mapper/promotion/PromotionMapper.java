package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;

import java.util.List;

public interface PromotionMapper {
    List<PromotionResponse> map(List<Promotion> promotions);

    PromotionResponse map(Promotion promotion);

    Promotion map(PromotionUpdateRequest request);

    Promotion map(PromotionCreateRequest request);
}
