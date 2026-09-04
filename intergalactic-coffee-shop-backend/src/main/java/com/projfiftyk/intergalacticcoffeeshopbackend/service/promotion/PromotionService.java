package com.projfiftyk.intergalacticcoffeeshopbackend.service.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.*;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;

import java.util.List;

public interface PromotionService {
    List<PromotionResponse> listPromotions();

    List<PromotionResponse> listPromotions(PromotionListRequest request, PromotionListFilterRequest filter);

    PromotionResponse getPromotion(Long id);

    PromotionResponse updatePromotion(Long id, PromotionUpdateRequest request);

    PromotionResponse updatePromotion(Long id, PromotionLifecycleUpdateRequest request);

    PromotionResponse createPromotion(PromotionCreateRequest request);
}
