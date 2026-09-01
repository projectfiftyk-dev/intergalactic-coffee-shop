package com.projfiftyk.intergalacticcoffeeshopbackend.service.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionRewardType;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionType;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.PromotionNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.promotion.PromotionMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.product.ProductRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.promotion.PromotionRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionLifecycleUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionServiceImplTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionMapper mapper;

    @Mock
    private ProductRepository productRepository;

    private PromotionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PromotionServiceImpl(
                promotionRepository,
                productRepository,
                mapper
        );
    }

    @Test
    void shouldListPromotions() {
        // Arrange
        Promotion first = createPromotion(1L);
        Promotion second = createPromotion(2L);

        List<Promotion> promotions =
                List.of(first, second);

        PromotionResponse firstResponse =
                createResponse(1L);

        PromotionResponse secondResponse =
                createResponse(2L);

        List<PromotionResponse> responses =
                List.of(firstResponse, secondResponse);

        when(promotionRepository.getPromotions())
                .thenReturn(promotions);

        when(mapper.map(promotions))
                .thenReturn(responses);

        // Act
        List<PromotionResponse> result =
                service.listPromotions();

        // Assert
        assertNotNull(result);
        assertEquals(responses, result);

        verify(promotionRepository)
                .getPromotions();

        verify(mapper)
                .map(promotions);
    }

    @Test
    void shouldGetPromotion() {
        // Arrange
        Promotion promotion =
                createPromotion(1L);

        PromotionResponse response =
                createResponse(1L);

        when(promotionRepository.getPromotion(1L))
                .thenReturn(promotion);

        when(mapper.map(promotion))
                .thenReturn(response);

        // Act
        PromotionResponse result =
                service.getPromotion(1L);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        verify(promotionRepository)
                .getPromotion(1L);

        verify(mapper)
                .map(promotion);
    }

    @Test
    void shouldUpdatePromotion() {
        // Arrange
        Long promotionId = 1L;

        Promotion existingPromotion =
                createPromotion(promotionId);

        PromotionUpdateRequest request =
                new PromotionUpdateRequest(
                        LocalDateTime.of(2026, 9, 1, 10, 0),
                        LocalDateTime.of(2026, 10, 1, 10, 0),
                        PromotionType.PRODUCT_DISCOUNT,
                        null,
                        null,
                        List.of(1L),
                        null,
                        PromotionRewardType.PERCENTAGE,
                        BigDecimal.valueOf(20)
                );

        Promotion promotionToUpdate =
                createPromotion(promotionId);

        Promotion updatedPromotion =
                createPromotion(promotionId);

        PromotionResponse response =
                createResponse(promotionId);

        when(promotionRepository.getPromotion(promotionId))
                .thenReturn(existingPromotion);

        when(mapper.map(request))
                .thenReturn(promotionToUpdate);

        when(promotionRepository.updatePromotion(
                promotionId,
                promotionToUpdate
        )).thenReturn(updatedPromotion);

        when(mapper.map(updatedPromotion))
                .thenReturn(response);

        // Act
        PromotionResponse result =
                service.updatePromotion(
                        promotionId,
                        request
                );

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        assertEquals(
                existingPromotion.getStatus(),
                promotionToUpdate.getStatus()
        );

        verify(promotionRepository)
                .getPromotion(promotionId);

        verify(mapper)
                .map(request);

        verify(promotionRepository)
                .updatePromotion(
                        promotionId,
                        promotionToUpdate
                );

        verify(mapper)
                .map(updatedPromotion);
    }

    @Test
    void shouldUpdatePromotionLifecycle() {
        // Arrange
        Long promotionId = 1L;

        Promotion promotion =
                createPromotion(promotionId);

        PromotionResponse response =
                createResponse(promotionId);

        PromotionLifecycleUpdateRequest request =
                new PromotionLifecycleUpdateRequest(
                        PromotionStatus.ACTIVE
                );

        when(promotionRepository.getPromotion(promotionId))
                .thenReturn(promotion);

        when(promotionRepository.updatePromotion(
                promotionId,
                promotion
        )).thenReturn(promotion);

        when(mapper.map(promotion))
                .thenReturn(response);

        // Act
        PromotionResponse result =
                service.updatePromotion(
                        promotionId,
                        request
                );

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        assertEquals(
                PromotionStatus.ACTIVE,
                promotion.getStatus()
        );

        verify(promotionRepository)
                .getPromotion(promotionId);

        verify(promotionRepository)
                .updatePromotion(
                        promotionId,
                        promotion
                );

        verify(mapper)
                .map(promotion);
    }

    private Promotion createPromotion(Long id) {
        Promotion promotion = new Promotion();

        promotion.setId(id);

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

    @Test
    void shouldThrowWhenPromotionDoesNotExist() {
        // Arrange
        Long promotionId = 99L;

        when(promotionRepository.getPromotion(promotionId))
                .thenReturn(null);

        // Act & Assert
        PromotionNotFoundException exception =
                assertThrows(
                        PromotionNotFoundException.class,
                        () -> service.getPromotion(promotionId)
                );

        assertEquals(
                "Promotion with id 99 was not found",
                exception.getMessage()
        );

        verify(promotionRepository)
                .getPromotion(promotionId);

        verifyNoInteractions(mapper);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingPromotion() {
        // Arrange
        Long promotionId = 99L;

        PromotionUpdateRequest request =
                new PromotionUpdateRequest(
                        LocalDateTime.of(2026, 9, 1, 10, 0),
                        LocalDateTime.of(2026, 10, 1, 10, 0),
                        PromotionType.PRODUCT_DISCOUNT,
                        null,
                        null,
                        List.of(1L),
                        null,
                        PromotionRewardType.PERCENTAGE,
                        BigDecimal.valueOf(20)
                );

        when(promotionRepository.getPromotion(promotionId))
                .thenReturn(null);

        // Act & Assert
        PromotionNotFoundException exception =
                assertThrows(
                        PromotionNotFoundException.class,
                        () -> service.updatePromotion(
                                promotionId,
                                request
                        )
                );

        assertEquals(
                "Promotion with id 99 was not found",
                exception.getMessage()
        );

        verify(promotionRepository)
                .getPromotion(promotionId);

        verifyNoInteractions(mapper);

        verify(promotionRepository, never())
                .updatePromotion(anyLong(), any());
    }

    @Test
    void shouldThrowWhenUpdatingLifecycleOfNonExistingPromotion() {
        // Arrange
        Long promotionId = 99L;

        PromotionLifecycleUpdateRequest request =
                new PromotionLifecycleUpdateRequest(
                        PromotionStatus.ACTIVE
                );

        when(promotionRepository.getPromotion(promotionId))
                .thenReturn(null);

        // Act & Assert
        PromotionNotFoundException exception =
                assertThrows(
                        PromotionNotFoundException.class,
                        () -> service.updatePromotion(
                                promotionId,
                                request
                        )
                );

        assertEquals(
                "Promotion with id 99 was not found",
                exception.getMessage()
        );

        verify(promotionRepository)
                .getPromotion(promotionId);

        verify(promotionRepository, never())
                .updatePromotion(anyLong(), any());

        verifyNoInteractions(mapper);
    }

    @Test
    void shouldCreatePromotion() {
        // Arrange
        PromotionCreateRequest request =
                new PromotionCreateRequest(
                        LocalDateTime.of(2026, 9, 1, 0, 0),
                        LocalDateTime.of(2026, 9, 30, 23, 59),
                        PromotionStatus.DRAFT,
                        PromotionType.NTH_PURCHASE,
                        5,
                        BigDecimal.ZERO,
                        List.of(1L, 2L),
                        List.of(1L),
                        PromotionRewardType.FIXED,
                        BigDecimal.valueOf(5)
                );

        Promotion promotion = createPromotion(null);

        Promotion createdPromotion = createPromotion(1L);

        PromotionResponse response = createResponse(1L);

        when(mapper.map(request))
                .thenReturn(promotion);

        when(promotionRepository.createPromotion(promotion))
                .thenReturn(createdPromotion);

        when(mapper.map(createdPromotion))
                .thenReturn(response);

        // Act
        PromotionResponse result =
                service.createPromotion(request);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        assertNotNull(promotion.getCreatedAt());

        verify(mapper)
                .map(request);

        verify(promotionRepository)
                .createPromotion(promotion);

        verify(mapper)
                .map(createdPromotion);
    }

    private PromotionResponse createResponse(Long id) {
        return new PromotionResponse(
                id,
                LocalDateTime.of(2026, 8, 30, 12, 0),
                LocalDateTime.of(2026, 9, 1, 0, 0),
                LocalDateTime.of(2026, 9, 30, 23, 59),
                PromotionStatus.DRAFT,
                PromotionType.NTH_PURCHASE,
                5,
                BigDecimal.ZERO,
                List.of(1L, 2L),
                List.of(1L),
                PromotionRewardType.FIXED,
                BigDecimal.valueOf(5)
        );
    }
}