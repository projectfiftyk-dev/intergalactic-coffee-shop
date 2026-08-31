package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionRewardType;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionType;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PromotionMapperImplTest {

    private PromotionMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new PromotionMapperImpl();
    }

    @Test
    void shouldMapPromotionToResponse() {
        // Arrange
        LocalDateTime createdAt =
                LocalDateTime.of(2026, 8, 31, 10, 0);

        LocalDateTime startDate =
                LocalDateTime.of(2026, 9, 1, 0, 0);

        LocalDateTime endDate =
                LocalDateTime.of(2026, 9, 30, 23, 59);

        Promotion promotion = new Promotion();

        promotion.setId(1L);
        promotion.setCreatedAt(createdAt);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setStatus(PromotionStatus.ACTIVE);
        promotion.setPromotionType(PromotionType.NTH_PURCHASE);
        promotion.setOccurrences(5);
        promotion.setMinimumValue(null);
        promotion.setProductIds(List.of(1L, 2L));
        promotion.setRequiredProducts(List.of(3L, 4L));
        promotion.setRewardType(PromotionRewardType.FIXED);
        promotion.setRewardValue(new BigDecimal("5.00"));

        // Act
        PromotionResponse result = mapper.map(promotion);

        // Assert
        assertNotNull(result);

        assertEquals(1L, result.id());
        assertEquals(createdAt, result.createdAt());
        assertEquals(startDate, result.startDate());
        assertEquals(endDate, result.endDate());

        assertEquals(
                PromotionStatus.ACTIVE,
                result.status()
        );

        assertEquals(
                PromotionType.NTH_PURCHASE,
                result.promotionType()
        );

        assertEquals(5, result.occurrences());
        assertNull(result.minimumValue());

        assertEquals(
                List.of(1L, 2L),
                result.productIds()
        );

        assertEquals(
                List.of(3L, 4L),
                result.requiredProducts()
        );

        assertEquals(
                PromotionRewardType.FIXED,
                result.rewardType()
        );

        assertEquals(
                new BigDecimal("5.00"),
                result.rewardValue()
        );
    }

    @Test
    void shouldMapPromotionListToResponseList() {
        // Arrange
        Promotion firstPromotion = new Promotion();
        firstPromotion.setId(1L);
        firstPromotion.setPromotionType(
                PromotionType.NTH_PURCHASE
        );
        firstPromotion.setOccurrences(5);
        firstPromotion.setRewardType(
                PromotionRewardType.FIXED
        );
        firstPromotion.setRewardValue(
                new BigDecimal("5.00")
        );

        Promotion secondPromotion = new Promotion();
        secondPromotion.setId(2L);
        secondPromotion.setPromotionType(
                PromotionType.PRODUCT_DISCOUNT
        );
        secondPromotion.setProductIds(List.of(2L));
        secondPromotion.setRewardType(
                PromotionRewardType.PERCENTAGE
        );
        secondPromotion.setRewardValue(
                new BigDecimal("20")
        );

        List<Promotion> promotions =
                List.of(firstPromotion, secondPromotion);

        // Act
        List<PromotionResponse> result =
                mapper.map(promotions);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).id());
        assertEquals(
                PromotionType.NTH_PURCHASE,
                result.get(0).promotionType()
        );
        assertEquals(
                PromotionRewardType.FIXED,
                result.get(0).rewardType()
        );
        assertEquals(
                new BigDecimal("5.00"),
                result.get(0).rewardValue()
        );

        assertEquals(2L, result.get(1).id());
        assertEquals(
                PromotionType.PRODUCT_DISCOUNT,
                result.get(1).promotionType()
        );
        assertEquals(
                List.of(2L),
                result.get(1).productIds()
        );
        assertEquals(
                PromotionRewardType.PERCENTAGE,
                result.get(1).rewardType()
        );
        assertEquals(
                new BigDecimal("20"),
                result.get(1).rewardValue()
        );
    }

    @Test
    void shouldMapUpdateRequestToPromotion() {
        // Arrange
        LocalDateTime startDate =
                LocalDateTime.of(2026, 9, 1, 0, 0);

        LocalDateTime endDate =
                LocalDateTime.of(2026, 9, 30, 23, 59);

        PromotionUpdateRequest request =
                new PromotionUpdateRequest(
                        startDate,
                        endDate,
                        PromotionType.MINIMUM_VALUE,
                        0,
                        new BigDecimal("30.00"),
                        List.of(1L, 2L),
                        List.of(3L),
                        PromotionRewardType.PERCENTAGE,
                        new BigDecimal("10")
                );

        // Act
        Promotion result = mapper.map(request);

        // Assert
        assertNotNull(result);

        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());

        assertEquals(
                PromotionType.MINIMUM_VALUE,
                result.getPromotionType()
        );

        assertEquals(0, result.getOccurrences());

        assertEquals(
                new BigDecimal("30.00"),
                result.getMinimumValue()
        );

        assertEquals(
                List.of(1L, 2L),
                result.getProductIds()
        );

        assertEquals(
                List.of(3L),
                result.getRequiredProducts()
        );

        assertEquals(
                PromotionRewardType.PERCENTAGE,
                result.getRewardType()
        );

        assertEquals(
                new BigDecimal("10"),
                result.getRewardValue()
        );
    }

    @Test
    void shouldMapEmptyPromotionList() {
        // Arrange
        List<Promotion> promotions = List.of();

        // Act
        List<PromotionResponse> result =
                mapper.map(promotions);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldMapCreateRequestToPromotion() {
        // Arrange
        LocalDateTime startDate =
                LocalDateTime.of(2026, 9, 1, 0, 0);

        LocalDateTime endDate =
                LocalDateTime.of(2026, 9, 30, 23, 59);

        PromotionCreateRequest request =
                new PromotionCreateRequest(
                        startDate,
                        endDate,
                        PromotionStatus.DRAFT,
                        PromotionType.MINIMUM_VALUE,
                        0,
                        new BigDecimal("30.00"),
                        List.of(1L, 2L),
                        List.of(3L),
                        PromotionRewardType.PERCENTAGE,
                        new BigDecimal("10.00")
                );

        // Act
        Promotion result = mapper.map(request);

        // Assert
        assertNotNull(result);

        assertNull(result.getId());
        assertNull(result.getCreatedAt());

        assertEquals(
                startDate,
                result.getStartDate()
        );

        assertEquals(
                endDate,
                result.getEndDate()
        );

        assertEquals(
                PromotionStatus.DRAFT,
                result.getStatus()
        );

        assertEquals(
                PromotionType.MINIMUM_VALUE,
                result.getPromotionType()
        );

        assertEquals(
                0,
                result.getOccurrences()
        );

        assertEquals(
                new BigDecimal("30.00"),
                result.getMinimumValue()
        );

        assertEquals(
                List.of(1L, 2L),
                result.getProductIds()
        );

        assertEquals(
                List.of(3L),
                result.getRequiredProducts()
        );

        assertEquals(
                PromotionRewardType.PERCENTAGE,
                result.getRewardType()
        );

        assertEquals(
                new BigDecimal("10.00"),
                result.getRewardValue()
        );
    }
}