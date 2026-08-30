package com.projfiftyk.intergalacticcoffeeshopbackend.repository.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;

import java.util.List;

public interface PromotionRepository {
    List<Promotion> getPromotions();

    Promotion getPromotion(Long id);

    Promotion updatePromotion(Long id, Promotion promotion);

    Promotion createPromotion(Promotion promotion);
}
