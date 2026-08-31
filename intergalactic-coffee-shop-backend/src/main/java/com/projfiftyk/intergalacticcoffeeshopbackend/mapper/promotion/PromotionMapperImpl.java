package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromotionMapperImpl implements PromotionMapper {

    @Override
    public List<PromotionResponse> map(List<Promotion> promotions) {
        return promotions.stream()
                .map(this::map)
                .toList();
    }

    public PromotionResponse map(Promotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                promotion.getCreatedAt(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getStatus(),
                promotion.getPromotionType(),
                promotion.getOccurrences(),
                promotion.getMinimumValue(),
                promotion.getProductIds(),
                promotion.getRequiredProducts(),
                promotion.getRewardType(),
                promotion.getRewardValue()
        );
    }

    @Override
    public Promotion map(PromotionUpdateRequest request) {
        Promotion promotion = new Promotion();

        promotion.setStartDate(request.startDate());
        promotion.setEndDate(request.endDate());
        promotion.setPromotionType(request.promotionType());
        promotion.setOccurrences(request.occurrences());
        promotion.setMinimumValue(request.minimumValue());
        promotion.setProductIds(request.productIds());
        promotion.setRequiredProducts(request.requiredProducts());
        promotion.setRewardType(request.rewardType());
        promotion.setRewardValue(request.rewardValue());

        return promotion;
    }
}