package com.projfiftyk.intergalacticcoffeeshopbackend.repository.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionRewardType;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JdbcPromotionRepositoryIntegrationTest {

    @Autowired
    private JdbcPromotionRepository repository;

    @Test
    void shouldCreatePromotion() {
        Promotion promotion = createPromotion();

        Promotion created = repository.createPromotion(promotion);

        assertNotNull(created);
        assertNotNull(created.getId());

        assertEquals(
                promotion.getCreatedAt(),
                created.getCreatedAt()
        );

        assertEquals(
                promotion.getStartDate(),
                created.getStartDate()
        );

        assertEquals(
                promotion.getEndDate(),
                created.getEndDate()
        );

        assertEquals(
                promotion.getStatus(),
                created.getStatus()
        );

        assertEquals(
                promotion.getPromotionType(),
                created.getPromotionType()
        );

        assertEquals(
                promotion.getOccurrences(),
                created.getOccurrences()
        );

        assertEquals(
                0,
                created.getMinimumValue().compareTo(created.getMinimumValue())
        );

        assertEquals(
                1,
                BigDecimal.valueOf(20).compareTo(created.getRewardValue())
        );

        assertEquals(
                0,
                BigDecimal.valueOf(5).compareTo(created.getRewardValue())
        );

        assertEquals(
                promotion.getProductIds(),
                created.getProductIds()
        );

        assertEquals(
                promotion.getRequiredProducts(),
                created.getRequiredProducts()
        );
    }

    @Test
    void shouldGetPromotionById() {
        Promotion created =
                repository.createPromotion(createPromotion());

        Promotion found =
                repository.getPromotion(created.getId());

        assertNotNull(found);

        assertEquals(
                created.getId(),
                found.getId()
        );

        assertEquals(
                created.getPromotionType(),
                found.getPromotionType()
        );

        assertEquals(
                created.getStatus(),
                found.getStatus()
        );

        assertEquals(
                created.getOccurrences(),
                found.getOccurrences()
        );

        assertEquals(
                created.getMinimumValue(),
                found.getMinimumValue()
        );

        assertEquals(
                created.getRewardType(),
                found.getRewardType()
        );

        assertEquals(
                created.getRewardValue(),
                found.getRewardValue()
        );

        assertEquals(
                created.getProductIds(),
                found.getProductIds()
        );

        assertEquals(
                created.getRequiredProducts(),
                found.getRequiredProducts()
        );
    }

    @Test
    void shouldReturnNullWhenPromotionDoesNotExist() {
        Promotion found =
                repository.getPromotion(999999L);

        assertNull(found);
    }

    @Test
    void shouldGetAllPromotions() {
        Promotion first =
                repository.createPromotion(createPromotion());

        Promotion second =
                repository.createPromotion(createPromotion());

        List<Promotion> promotions =
                repository.getPromotions();

        assertEquals(2, promotions.size());

        assertEquals(
                first.getId(),
                promotions.get(0).getId()
        );

        assertEquals(
                second.getId(),
                promotions.get(1).getId()
        );
    }

    @Test
    void shouldUpdatePromotion() {
        Promotion created =
                repository.createPromotion(createPromotion());

        Promotion updated = createPromotion();

        updated.setStartDate(
                LocalDateTime.of(2026, 9, 1, 10, 0)
        );

        updated.setEndDate(
                LocalDateTime.of(2026, 10, 1, 10, 0)
        );

        updated.setStatus(
                PromotionStatus.DEPRECATED
        );

        updated.setPromotionType(
                PromotionType.PRODUCT_DISCOUNT
        );

        updated.setRewardType(
                PromotionRewardType.PERCENTAGE
        );

        updated.setRewardValue(
                BigDecimal.valueOf(20)
        );

        Promotion result =
                repository.updatePromotion(
                        created.getId(),
                        updated
                );

        assertNotNull(result);

        assertEquals(
                created.getId(),
                result.getId()
        );

        assertEquals(
                updated.getStartDate(),
                result.getStartDate()
        );

        assertEquals(
                updated.getEndDate(),
                result.getEndDate()
        );

        assertEquals(
                updated.getStatus(),
                result.getStatus()
        );

        assertEquals(
                updated.getPromotionType(),
                result.getPromotionType()
        );

        assertEquals(
                0,
                updated.getMinimumValue().compareTo(result.getMinimumValue())
        );

        assertEquals(
                0,
                BigDecimal.valueOf(20).compareTo(result.getRewardValue())
        );

        assertEquals(
                updated.getRewardType(),
                result.getRewardType()
        );

        assertEquals(
                0,
                updated.getMinimumValue().compareTo(result.getMinimumValue())
        );

        assertEquals(
                0,
                BigDecimal.valueOf(20).compareTo(result.getRewardValue())
        );

        assertEquals(
                updated.getRequiredProducts(),
                result.getRequiredProducts()
        );
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistingPromotion() {
        Promotion promotion = createPromotion();

        Promotion result =
                repository.updatePromotion(
                        999999L,
                        promotion
                );

        assertNull(result);
    }

    @Test
    void shouldUpdateTargetAndRequiredProducts() {
        Promotion created =
                repository.createPromotion(
                        createPromotion()
                );

        Promotion updated = createPromotion();

        updated.setProductIds(
                List.of(2L)
        );

        updated.setRequiredProducts(
                List.of(1L)
        );

        Promotion result =
                repository.updatePromotion(
                        created.getId(),
                        updated
                );

        assertNotNull(result);

        assertEquals(
                List.of(2L),
                result.getProductIds()
        );

        assertEquals(
                List.of(1L),
                result.getRequiredProducts()
        );
    }

    private Promotion createPromotion() {
        Promotion promotion = new Promotion();

        promotion.setCreatedAt(
                LocalDateTime.of(2026, 8, 30, 12, 0)
        );

        promotion.setStartDate(
                LocalDateTime.of(2026, 9, 1, 0, 0)
        );

        promotion.setEndDate(
                LocalDateTime.of(2026, 9, 30, 23, 59)
        );

        promotion.setStatus(
                PromotionStatus.DRAFT
        );

        promotion.setPromotionType(
                PromotionType.NTH_PURCHASE
        );

        promotion.setOccurrences(5);

        promotion.setMinimumValue(
                BigDecimal.ZERO
        );

        promotion.setProductIds(
                List.of(1L, 2L)
        );

        promotion.setRequiredProducts(
                List.of(1L)
        );

        promotion.setRewardType(
                PromotionRewardType.FIXED
        );

        promotion.setRewardValue(
                BigDecimal.valueOf(5)
        );

        return promotion;
    }
}