package com.projfiftyk.intergalacticcoffeeshopbackend.web.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionRewardType;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionType;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Role;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.PromotionNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.promotion.PromotionService;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.security.SessionService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionLifecycleUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionListFilterRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionListRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.request.PromotionUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.promotion.response.PromotionResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.web.security.SecurityContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PromotionController.class
)
class PromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PromotionService promotionService;

    @MockitoBean
    private SecurityContext securityContext;

    @MockitoBean
    private SessionService sessionService;

    @Test
    void shouldListPromotionsForAdmin() throws Exception {
        // Arrange
        PromotionListRequest request = new PromotionListRequest(
                1,
                10,
                PromotionSortField.CREATED_AT,
                SortDirection.ASC
        );

        PromotionListFilterRequest filter = new PromotionListFilterRequest(
                LocalDateTime.of(2026, 8, 1, 0, 0),
                LocalDateTime.of(2026, 8, 31, 23, 59),
                LocalDateTime.of(2026, 9, 1, 0, 0),
                LocalDateTime.of(2026, 9, 30, 23, 59),
                List.of(
                        PromotionStatus.DRAFT,
                        PromotionStatus.ACTIVE
                )
        );

        List<PromotionResponse> promotions = List.of(
                new PromotionResponse(
                        1L,
                        LocalDateTime.of(2026, 8, 30, 10, 0),
                        LocalDateTime.of(2026, 9, 1, 0, 0),
                        LocalDateTime.of(2026, 9, 30, 23, 59),
                        PromotionStatus.DRAFT,
                        PromotionType.NTH_PURCHASE,
                        5,
                        BigDecimal.ZERO,
                        List.of(),
                        List.of(),
                        PromotionRewardType.FIXED,
                        BigDecimal.valueOf(5)
                ),
                new PromotionResponse(
                        2L,
                        LocalDateTime.of(2026, 8, 30, 11, 0),
                        LocalDateTime.of(2026, 9, 1, 0, 0),
                        LocalDateTime.of(2026, 9, 30, 23, 59),
                        PromotionStatus.ACTIVE,
                        PromotionType.NTH_PURCHASE,
                        3,
                        BigDecimal.ZERO,
                        List.of(),
                        List.of(),
                        PromotionRewardType.FIXED,
                        BigDecimal.valueOf(10)
                )
        );

        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(promotionService.listPromotions(any(PromotionListRequest.class), any(PromotionListFilterRequest.class)))
                .thenReturn(promotions);

        // Act & Assert
        mockMvc.perform(
                        get("/promotions")
                                .param("pageNumber", "1")
                                .param("pageSize", "10")
                                .param("sortField", "CREATED_AT")
                                .param("direction", "ASC")
                                .param("createdAtFrom", "2026-08-01T00:00:00")
                                .param("createdAtTo", "2026-08-31T23:59:00")
                                .param("startDateFrom", "2026-09-01T00:00:00")
                                .param("startDateTo", "2026-09-30T23:59:00")
                                .param("statuses", "DRAFT", "ACTIVE")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("DRAFT"))
                .andExpect(jsonPath("$[0].promotionType").value("NTH_PURCHASE"))
                .andExpect(jsonPath("$[0].occurrences").value(5))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].promotionType").value("NTH_PURCHASE"))
                .andExpect(jsonPath("$[1].occurrences").value(3));

        verify(promotionService).listPromotions(request, filter);
    }

    @Test
    void shouldGetPromotionForAdmin() throws Exception {
        // Arrange
        PromotionResponse promotion = new PromotionResponse(
                1L,
                LocalDateTime.of(2026, 8, 30, 10, 0),
                LocalDateTime.of(2026, 9, 1, 0, 0),
                LocalDateTime.of(2026, 9, 30, 23, 59),
                PromotionStatus.DRAFT,
                PromotionType.NTH_PURCHASE,
                5,
                BigDecimal.ZERO,
                List.of(),
                List.of(),
                PromotionRewardType.FIXED,
                BigDecimal.valueOf(5)
        );

        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(promotionService.getPromotion(1L)).thenReturn(promotion);

        // Act & Assert
        mockMvc.perform(
                        get("/promotions/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("status").value("DRAFT"))
                .andExpect(jsonPath("promotionType").value("NTH_PURCHASE"))
                .andExpect(jsonPath("occurrences").value(5))
                .andExpect(jsonPath("rewardType").value("FIXED"))
                .andExpect(jsonPath("rewardValue").value(5));

        verify(promotionService).getPromotion(1L);
    }

    @Test
    void shouldReturnNotFoundWhenAdminGetsNonExistingPromotion() throws Exception {
        // Arrange
        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(promotionService.getPromotion(1L))
                .thenThrow(PromotionNotFoundException.class);

        // Act & Assert
        mockMvc.perform(
                        get("/promotions/1")
                )
                .andExpect(status().isNotFound());

        verify(promotionService).getPromotion(1L);
    }

    @Test
    void shouldCreatePromotionForAdmin() throws Exception {
        // Arrange
        PromotionCreateRequest request = new PromotionCreateRequest(
                LocalDateTime.of(2026, 9, 1, 0, 0),
                LocalDateTime.of(2026, 9, 30, 23, 59),
                PromotionStatus.DRAFT,
                PromotionType.NTH_PURCHASE,
                5,
                BigDecimal.ZERO,
                List.of(),
                List.of(),
                PromotionRewardType.FIXED,
                BigDecimal.valueOf(5)
        );

        PromotionResponse promotion = new PromotionResponse(
                1L,
                LocalDateTime.of(2026, 8, 30, 10, 0),
                LocalDateTime.of(2026, 9, 1, 0, 0),
                LocalDateTime.of(2026, 9, 30, 23, 59),
                PromotionStatus.DRAFT,
                PromotionType.NTH_PURCHASE,
                5,
                BigDecimal.ZERO,
                List.of(),
                List.of(),
                PromotionRewardType.FIXED,
                BigDecimal.valueOf(5)
        );

        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(promotionService.createPromotion(request)).thenReturn(promotion);

        // Act & Assert
        mockMvc.perform(
                        post("/promotions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "startDate": "2026-09-01T00:00:00",
                                            "endDate": "2026-09-30T23:59:00",
                                            "status": "DRAFT",
                                            "promotionType": "NTH_PURCHASE",
                                            "occurrences": 5,
                                            "minimumValue": 0,
                                            "productIds": [],
                                            "requiredProducts": [],
                                            "rewardType": "FIXED",
                                            "rewardValue": 5
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("status").value("DRAFT"))
                .andExpect(jsonPath("promotionType").value("NTH_PURCHASE"))
                .andExpect(jsonPath("occurrences").value(5))
                .andExpect(jsonPath("rewardType").value("FIXED"))
                .andExpect(jsonPath("rewardValue").value(5));

        verify(promotionService).createPromotion(request);
    }

    @Test
    void shouldUpdatePromotionForAdmin() throws Exception {
        // Arrange
        PromotionUpdateRequest request = new PromotionUpdateRequest(
                LocalDateTime.of(2026, 9, 1, 0, 0),
                LocalDateTime.of(2026, 9, 30, 23, 59),
                PromotionType.NTH_PURCHASE,
                10,
                BigDecimal.valueOf(20),
                List.of(),
                List.of(),
                PromotionRewardType.FIXED,
                BigDecimal.valueOf(10)
        );

        PromotionResponse promotion = new PromotionResponse(
                1L,
                LocalDateTime.of(2026, 8, 30, 10, 0),
                LocalDateTime.of(2026, 9, 1, 0, 0),
                LocalDateTime.of(2026, 9, 30, 23, 59),
                PromotionStatus.DRAFT,
                PromotionType.NTH_PURCHASE,
                10,
                BigDecimal.valueOf(20),
                List.of(),
                List.of(),
                PromotionRewardType.FIXED,
                BigDecimal.valueOf(10)
        );

        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(promotionService.updatePromotion(1L, request)).thenReturn(promotion);

        // Act & Assert
        mockMvc.perform(
                        put("/promotions/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "startDate": "2026-09-01T00:00:00",
                                            "endDate": "2026-09-30T23:59:00",
                                            "promotionType": "NTH_PURCHASE",
                                            "occurrences": 10,
                                            "minimumValue": 20,
                                            "productIds": [],
                                            "requiredProducts": [],
                                            "rewardType": "FIXED",
                                            "rewardValue": 10
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("status").value("DRAFT"))
                .andExpect(jsonPath("promotionType").value("NTH_PURCHASE"))
                .andExpect(jsonPath("occurrences").value(10))
                .andExpect(jsonPath("minimumValue").value(20))
                .andExpect(jsonPath("rewardValue").value(10));

        verify(promotionService).updatePromotion(1L, request);
    }

    @Test
    void shouldUpdatePromotionStatusForAdmin() throws Exception {
        // Arrange
        PromotionLifecycleUpdateRequest request =
                new PromotionLifecycleUpdateRequest(
                        PromotionStatus.ACTIVE
                );

        PromotionResponse promotion = new PromotionResponse(
                1L,
                LocalDateTime.of(2026, 8, 30, 10, 0),
                LocalDateTime.of(2026, 9, 1, 0, 0),
                LocalDateTime.of(2026, 9, 30, 23, 59),
                PromotionStatus.ACTIVE,
                PromotionType.NTH_PURCHASE,
                5,
                BigDecimal.ZERO,
                List.of(),
                List.of(),
                PromotionRewardType.FIXED,
                BigDecimal.valueOf(5)
        );

        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(promotionService.updatePromotion(1L, request)).thenReturn(promotion);

        // Act & Assert
        mockMvc.perform(
                        patch("/promotions/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "status": "ACTIVE"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("status").value("ACTIVE"));

        verify(promotionService).updatePromotion(1L, request);
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        // Arrange
        when(securityContext.isAuthenticated()).thenReturn(false);

        // Act & Assert
        mockMvc.perform(
                        get("/promotions")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {
        // Arrange
        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(
                        get("/promotions")
                )
                .andExpect(status().isForbidden());
    }
}