package com.projfiftyk.intergalacticcoffeeshopbackend.service.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionLifecycleUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;

import java.util.List;

public interface PromotionService {
    List<PromotionResponse> listPromotions();

    PromotionResponse getPromotion(Long id);

    PromotionResponse updatePromotion(Long id, PromotionUpdateRequest request);

    PromotionResponse updatePromotion(Long id, PromotionLifecycleUpdateRequest request);
}
