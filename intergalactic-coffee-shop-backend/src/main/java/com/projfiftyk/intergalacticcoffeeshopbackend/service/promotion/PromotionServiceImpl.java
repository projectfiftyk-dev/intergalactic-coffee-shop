package com.projfiftyk.intergalacticcoffeeshopbackend.service.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionRewardType;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionType;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.PromotionInvalidException;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.PromotionNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.promotion.PromotionMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.product.ProductRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.promotion.PromotionRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.*;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    private final PromotionMapper mapper;

    public PromotionServiceImpl(
            PromotionRepository promotionRepository,
            ProductRepository productRepository,
            PromotionMapper mapper)
    {
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PromotionResponse> listPromotions() {
        List<Promotion> promotions = promotionRepository.getPromotions();
        return mapper.map(promotions);
    }

    @Override
    public List<PromotionResponse> listPromotions(PromotionListRequest request, PromotionListFilterRequest filter) {
        List<Promotion> promotions = promotionRepository.getPromotions(
                (request.pageNumber() - 1) * request.pageSize(),
                request.pageSize(),
                request.sortField(),
                request.direction(),
                filter.createdAtFrom(),
                filter.createdAtTo(),
                filter.startDateFrom(),
                filter.startDateTo(),
                filter.statuses()
        );
        return mapper.map(promotions);
    }

    @Override
    public PromotionResponse getPromotion(Long id) {
        Promotion promotion = promotionRepository.getPromotion(id);
        if (promotion == null)
            throw new PromotionNotFoundException(id);

        return mapper.map(promotion);
    }

    @Override
    public PromotionResponse updatePromotion(Long id, PromotionUpdateRequest request) {
        Promotion promotion = promotionRepository.getPromotion(id);
        if (promotion == null)
            throw new PromotionNotFoundException(id);

        Promotion promotionToUpdate = mapper.map(request);
        promotionToUpdate.setStatus(promotion.getStatus());
        Promotion resulted = promotionRepository.updatePromotion(id, promotionToUpdate);
        return mapper.map(resulted);
    }

    @Override
    public PromotionResponse updatePromotion(Long id, PromotionLifecycleUpdateRequest request) {
        Promotion promotion = promotionRepository.getPromotion(id);
        if (promotion == null)
            throw new PromotionNotFoundException(id);

        promotion.setStatus(request.status());
        Promotion resulted = promotionRepository.updatePromotion(id, promotion);
        return mapper.map(resulted);
    }

    @Override
    public PromotionResponse createPromotion(PromotionCreateRequest request) {
        Promotion promotion = mapper.map(request);

        promotion.setCreatedAt(LocalDateTime.now());

        Promotion created = promotionRepository.createPromotion(promotion);

        return mapper.map(created);
    }

    private void validatePromotion(PromotionCreateRequest request) {
        if (request.productIds() != null && !request.productIds().isEmpty()) {
            request.productIds().forEach(productRepository::getProduct);
        }

        if (request.requiredProducts() != null && !request.requiredProducts().isEmpty()) {
            request.requiredProducts().forEach(productRepository::getProduct);
        }

        if (request.promotionType() == PromotionType.PRODUCT_DISCOUNT
                && request.rewardType() == PromotionRewardType.FIXED) {
            throw new PromotionInvalidException(
                    "PRODUCT_DISCOUNT promotions cannot have a FIXED reward type"
            );
        }
    }
}
