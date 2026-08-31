package com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Promotion {

    private Long id;
    private LocalDateTime createdAt;

    // Validity
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Lifecycle
    private PromotionStatus status;

    // Type
    private PromotionType promotionType;

    // NTH_PURCHASE
    private Integer occurrences;

    // MINIMUM_VALUE
    private BigDecimal minimumValue;

    // PRODUCT_DISCOUNT
    private List<Long> productIds;

    // Required products
    private List<Long> requiredProducts;

    // Reward
    private PromotionRewardType rewardType;
    private BigDecimal rewardValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public PromotionStatus getStatus() {
        return status;
    }

    public void setStatus(PromotionStatus status) {
        this.status = status;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public Integer getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Integer occurrences) {
        this.occurrences = occurrences;
    }

    public BigDecimal getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(BigDecimal minimumValue) {
        this.minimumValue = minimumValue;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<Long> getRequiredProducts() {
        return requiredProducts;
    }

    public void setRequiredProducts(List<Long> requiredProducts) {
        this.requiredProducts = requiredProducts;
    }

    public PromotionRewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(PromotionRewardType rewardType) {
        this.rewardType = rewardType;
    }

    public BigDecimal getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(BigDecimal rewardValue) {
        this.rewardValue = rewardValue;
    }
}