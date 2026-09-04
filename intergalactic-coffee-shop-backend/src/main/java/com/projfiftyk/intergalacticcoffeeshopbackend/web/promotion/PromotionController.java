package com.projfiftyk.intergalacticcoffeeshopbackend.web.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Role;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.promotion.PromotionService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.*;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.web.security.RequireRole;
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
    @RequireRole(Role.ADMIN)
    public List<PromotionResponse> listPromotions(
            @ModelAttribute PromotionListRequest request,
            @ModelAttribute PromotionListFilterRequest filter
            ) {
        return promotionService.listPromotions(request, filter);
    }

    @GetMapping("/{id}")
    @RequireRole(Role.ADMIN)
    public PromotionResponse getPromotion(@PathVariable Long id)
    {
        return promotionService.getPromotion(id);
    }

    @PutMapping("/{id}")
    @RequireRole(Role.ADMIN)
    public PromotionResponse updatePromotion(
            @PathVariable Long id,
            @Valid @RequestBody PromotionUpdateRequest request
            )
    {
        return promotionService.updatePromotion(id, request);
    }

    @PatchMapping("/{id}")
    @RequireRole(Role.ADMIN)
    public PromotionResponse updatePromotionStatus(
            @PathVariable Long id,
            @Valid @RequestBody PromotionLifecycleUpdateRequest request
            )
    {
        return promotionService.updatePromotion(id, request);
    }

    @PostMapping()
    @RequireRole(Role.ADMIN)
    public PromotionResponse createPromotion(@Valid @RequestBody PromotionCreateRequest request)
    {
        return promotionService.createPromotion(request);
    }
}
