package com.projfiftyk.intergalacticcoffeeshopbackend.service.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.promotion.PromotionMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.promotion.PromotionRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionLifecycleUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper mapper;

    public PromotionServiceImpl(
            PromotionRepository promotionRepository,
            PromotionMapper mapper)
    {
        this.promotionRepository = promotionRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PromotionResponse> listPromotions() {
        List<Promotion> promotions = promotionRepository.getPromotions();
        return mapper.map(promotions);
    }

    @Override
    public PromotionResponse getPromotion(Long id) {
        Promotion promotion = promotionRepository.getPromotion(id);
        return mapper.map(promotion);
    }

    @Override
    public PromotionResponse updatePromotion(Long id, PromotionUpdateRequest request) {
        Promotion promotion = promotionRepository.getPromotion(id);
        Promotion promotionToUpdate = mapper.map(request);
        promotionToUpdate.setStatus(promotion.getStatus());
        Promotion resulted = promotionRepository.updatePromotion(id, promotionToUpdate);
        return mapper.map(resulted);
    }

    @Override
    public PromotionResponse updatePromotion(Long id, PromotionLifecycleUpdateRequest request) {
        Promotion promotion = promotionRepository.getPromotion(id);
        promotion.setStatus(request.status());
        Promotion resulted = promotionRepository.updatePromotion(id, promotion);
        return mapper.map(resulted);
    }
}
