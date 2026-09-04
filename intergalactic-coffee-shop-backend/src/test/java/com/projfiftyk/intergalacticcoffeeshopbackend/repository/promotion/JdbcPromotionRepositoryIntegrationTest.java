package com.projfiftyk.intergalacticcoffeeshopbackend.repository.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JdbcPromotionRepositoryIntegrationTest {

    @Autowired
    private JdbcPromotionRepository repository;

    @Test
    void shouldGetPromotionById() {
        List<Promotion> promotions = repository.getPromotions();

        assertEquals(3, promotions.size());

        Promotion expected = promotions.get(0);

        Promotion found =
                repository.getPromotion(expected.getId());

        assertNotNull(found);

        assertEquals(
                expected.getId(),
                found.getId()
        );

        assertEquals(
                expected.getCreatedAt(),
                found.getCreatedAt()
        );

        assertEquals(
                expected.getStartDate(),
                found.getStartDate()
        );

        assertEquals(
                expected.getEndDate(),
                found.getEndDate()
        );

        assertEquals(
                expected.getStatus(),
                found.getStatus()
        );

        assertEquals(
                expected.getPromotionType(),
                found.getPromotionType()
        );

        assertEquals(
                expected.getOccurrences(),
                found.getOccurrences()
        );

        assertEquals(
                expected.getMinimumValue(),
                found.getMinimumValue()
        );

        assertEquals(
                expected.getRewardType(),
                found.getRewardType()
        );

        assertEquals(
                expected.getRewardValue(),
                found.getRewardValue()
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
        List<Promotion> promotions =
                repository.getPromotions();

        assertEquals(3, promotions.size());

        assertEquals(
                PromotionStatus.DRAFT,
                promotions.get(0).getStatus()
        );

        assertEquals(
                PromotionStatus.ACTIVE,
                promotions.get(1).getStatus()
        );

        assertEquals(
                PromotionStatus.DEPRECATED,
                promotions.get(2).getStatus()
        );
    }

    @Test
    void shouldGetPromotionsWithPagination() {
        List<Promotion> promotions =
                repository.getPromotions(
                        0,
                        2,
                        PromotionSortField.CREATED_AT,
                        SortDirection.ASC,
                        null,
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(2, promotions.size());

        assertEquals(
                PromotionStatus.DRAFT,
                promotions.get(0).getStatus()
        );

        assertEquals(
                PromotionStatus.ACTIVE,
                promotions.get(1).getStatus()
        );
    }

    @Test
    void shouldGetPromotionsWithOffset() {
        List<Promotion> promotions =
                repository.getPromotions(
                        2,
                        10,
                        PromotionSortField.CREATED_AT,
                        SortDirection.ASC,
                        null,
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(1, promotions.size());

        assertEquals(
                PromotionStatus.DEPRECATED,
                promotions.get(0).getStatus()
        );
    }

    @Test
    void shouldGetPromotionsWithStatusFilter() {
        List<Promotion> promotions =
                repository.getPromotions(
                        0,
                        10,
                        PromotionSortField.CREATED_AT,
                        SortDirection.ASC,
                        null,
                        null,
                        null,
                        null,
                        List.of(PromotionStatus.ACTIVE)
                );

        assertEquals(1, promotions.size());

        assertEquals(
                PromotionStatus.ACTIVE,
                promotions.get(0).getStatus()
        );
    }

    @Test
    void shouldGetPromotionsWithMultipleStatusFilter() {
        List<Promotion> promotions =
                repository.getPromotions(
                        0,
                        10,
                        PromotionSortField.CREATED_AT,
                        SortDirection.ASC,
                        null,
                        null,
                        null,
                        null,
                        List.of(
                                PromotionStatus.DRAFT,
                                PromotionStatus.ACTIVE
                        )
                );

        assertEquals(2, promotions.size());

        assertEquals(
                PromotionStatus.DRAFT,
                promotions.get(0).getStatus()
        );

        assertEquals(
                PromotionStatus.ACTIVE,
                promotions.get(1).getStatus()
        );
    }

    @Test
    void shouldGetPromotionsWithCreatedAtFromFilter() {
        List<Promotion> promotions =
                repository.getPromotions(
                        0,
                        10,
                        PromotionSortField.CREATED_AT,
                        SortDirection.ASC,
                        LocalDateTime.of(2026, 8, 30, 11, 0),
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(2, promotions.size());

        assertEquals(
                PromotionStatus.ACTIVE,
                promotions.get(0).getStatus()
        );

        assertEquals(
                PromotionStatus.DEPRECATED,
                promotions.get(1).getStatus()
        );
    }

    @Test
    void shouldGetPromotionsWithCreatedAtToFilter() {
        List<Promotion> promotions =
                repository.getPromotions(
                        0,
                        10,
                        PromotionSortField.CREATED_AT,
                        SortDirection.ASC,
                        null,
                        LocalDateTime.of(2026, 8, 30, 11, 0),
                        null,
                        null,
                        null
                );

        assertEquals(2, promotions.size());

        assertEquals(
                PromotionStatus.DRAFT,
                promotions.get(0).getStatus()
        );

        assertEquals(
                PromotionStatus.ACTIVE,
                promotions.get(1).getStatus()
        );
    }

    @Test
    void shouldGetPromotionsWithStartDateFromFilter() {
        List<Promotion> promotions =
                repository.getPromotions(
                        0,
                        10,
                        PromotionSortField.START_DATE,
                        SortDirection.ASC,
                        null,
                        null,
                        LocalDateTime.of(2026, 9, 5, 0, 0),
                        null,
                        null
                );

        assertEquals(1, promotions.size());

        assertEquals(
                PromotionStatus.DEPRECATED,
                promotions.get(0).getStatus()
        );
    }

    @Test
    void shouldGetPromotionsWithStartDateToFilter() {
        List<Promotion> promotions =
                repository.getPromotions(
                        0,
                        10,
                        PromotionSortField.START_DATE,
                        SortDirection.ASC,
                        null,
                        null,
                        null,
                        LocalDateTime.of(2026, 9, 1, 0, 0),
                        null
                );

        assertEquals(2, promotions.size());

        assertEquals(
                PromotionStatus.DRAFT,
                promotions.get(0).getStatus()
        );

        assertEquals(
                PromotionStatus.ACTIVE,
                promotions.get(1).getStatus()
        );
    }

    @Test
    void shouldSortPromotionsDescendingByCreatedAt() {
        List<Promotion> promotions =
                repository.getPromotions(
                        0,
                        10,
                        PromotionSortField.CREATED_AT,
                        SortDirection.DESC,
                        null,
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(3, promotions.size());

        assertEquals(
                PromotionStatus.DEPRECATED,
                promotions.get(0).getStatus()
        );

        assertEquals(
                PromotionStatus.ACTIVE,
                promotions.get(1).getStatus()
        );

        assertEquals(
                PromotionStatus.DRAFT,
                promotions.get(2).getStatus()
        );
    }

    @Test
    void shouldCombineFiltersAndPagination() {
        List<Promotion> promotions =
                repository.getPromotions(
                        0,
                        1,
                        PromotionSortField.CREATED_AT,
                        SortDirection.ASC,
                        LocalDateTime.of(2026, 8, 30, 10, 0),
                        null,
                        null,
                        null,
                        List.of(
                                PromotionStatus.DRAFT,
                                PromotionStatus.ACTIVE
                        )
                );

        assertEquals(1, promotions.size());

        assertEquals(
                PromotionStatus.DRAFT,
                promotions.get(0).getStatus()
        );
    }
}