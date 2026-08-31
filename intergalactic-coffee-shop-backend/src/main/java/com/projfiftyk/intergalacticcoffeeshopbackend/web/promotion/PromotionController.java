package com.projfiftyk.intergalacticcoffeeshopbackend.web.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.service.promotion.PromotionService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionLifecycleUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotions")
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService)
    {
        this.promotionService = promotionService;
    }

    @GetMapping
    public List<PromotionResponse> listPromotions() {
        return promotionService.listPromotions();
    }

    @GetMapping("/{id}")
    public PromotionResponse getPromotion(@PathVariable Long id)
    {
        return promotionService.getPromotion(id);
    }

    @PutMapping("/{id}")
    public PromotionResponse updatePromotion(
            @PathVariable Long id,
            @Valid @RequestBody PromotionUpdateRequest request
            )
    {
        return promotionService.updatePromotion(id, request);
    }

    @PatchMapping("/{id}")
    public PromotionResponse updatePromotionStatus(
            @PathVariable Long id,
            @Valid @RequestBody PromotionLifecycleUpdateRequest request
            )
    {
        return promotionService.updatePromotion(id, request);
    }

    @PostMapping()
    public PromotionResponse createPromtion(@Valid @RequestBody PromotionCreateRequest request)
    {
        return promotionService.createPromotion(request);
    }
}
