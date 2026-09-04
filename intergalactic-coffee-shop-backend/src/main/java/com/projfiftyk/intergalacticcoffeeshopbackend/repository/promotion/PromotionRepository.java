package com.projfiftyk.intergalacticcoffeeshopbackend.repository.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface PromotionRepository {
    List<Promotion> getPromotions();

    List<Promotion> getPromotions(int offset, int limit, PromotionSortField sortField,
                                  SortDirection sortDirection, LocalDateTime createdAtFrom,
                                  LocalDateTime createdAtTo,
                                  LocalDateTime startedAtFrom,
                                  LocalDateTime startedAtTo,
                                  List<PromotionStatus> statuses);

    Promotion getPromotion(Long id);

    Promotion updatePromotion(Long id, Promotion promotion);

    Promotion createPromotion(Promotion promotion);
}
