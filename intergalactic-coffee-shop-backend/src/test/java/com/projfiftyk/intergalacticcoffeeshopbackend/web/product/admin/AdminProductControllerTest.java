package com.projfiftyk.intergalacticcoffeeshopbackend.web.product.admin;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Role;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.ProductNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.product.ProductService;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.security.SessionService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductListRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.web.security.SecurityContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AdminProductController.class
)
class AdminProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private SecurityContext securityContext;

    @MockitoBean
    private SessionService sessionService;

    @Test
    void shouldListProductsForAdmin() throws Exception {
        // Arrange
        ProductListRequest request = new ProductListRequest(
                1,
                10,
                ProductSortField.NAME,
                SortDirection.ASC
        );

        List<ProductResponse> products = List.of(
                new ProductResponse(
                        1L,
                        1L,
                        "Espresso",
                        ProductStatus.ACTIVE
                ),
                new ProductResponse(
                        2L,
                        2L,
                        "Cappuccino",
                        ProductStatus.DEPRECATED
                )
        );

        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(productService.listProducts(request)).thenReturn(products);

        // Act & Assert
        mockMvc.perform(
                        get("/admin/products")
                                .param("pageNumber", "1")
                                .param("pageSize", "10")
                                .param("sortField", "NAME")
                                .param("direction", "ASC")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].version").value(1))
                .andExpect(jsonPath("$[0].name").value("Espresso"))
                .andExpect(jsonPath("$[0].productStatus").value("ACTIVE"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].version").value(2))
                .andExpect(jsonPath("$[1].name").value("Cappuccino"))
                .andExpect(jsonPath("$[1].productStatus").value("DEPRECATED"));

        verify(productService).listProducts(request);
    }

    @Test
    void shouldGetProductForAdmin() throws Exception {
        // Arrange
        ProductResponse product = new ProductResponse(
                1L,
                3L,
                "Espresso",
                ProductStatus.ACTIVE
        );

        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(productService.getProduct(1L)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(
                        get("/admin/products/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("version").value(3))
                .andExpect(jsonPath("name").value("Espresso"))
                .andExpect(jsonPath("productStatus").value("ACTIVE"));

        verify(productService).getProduct(1L);
    }

    @Test
    void shouldReturnNotFoundWhenAdminGetsNonExistingProduct() throws Exception {
        // Arrange
        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(productService.getProduct(1L))
                .thenThrow(ProductNotFoundException.class);

        // Act & Assert
        mockMvc.perform(
                        get("/admin/products/1")
                )
                .andExpect(status().isNotFound());

        verify(productService).getProduct(1L);
    }

    @Test
    void shouldCreateProductForAdmin() throws Exception {
        // Arrange
        ProductResponse product = new ProductResponse(
                1L,
                1L,
                "Espresso",
                ProductStatus.DRAFT
        );

        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(productService.createProduct(
                new ProductCreateRequest("Espresso")
        )).thenReturn(product);

        // Act & Assert
        mockMvc.perform(
                        post("/admin/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Espresso"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("version").value(1))
                .andExpect(jsonPath("name").value("Espresso"))
                .andExpect(jsonPath("productStatus").value("DRAFT"));

        verify(productService).createProduct(
                new ProductCreateRequest("Espresso")
        );
    }

    @Test
    void shouldUpdateProductForAdmin() throws Exception {
        // Arrange
        ProductResponse product = new ProductResponse(
                1L,
                2L,
                "Latte",
                ProductStatus.ACTIVE
        );

        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(productService.updateProduct(
                1L,
                new ProductUpdateRequest("Latte")
        )).thenReturn(product);

        // Act & Assert
        mockMvc.perform(
                        put("/admin/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Latte"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("version").value(2))
                .andExpect(jsonPath("name").value("Latte"))
                .andExpect(jsonPath("productStatus").value("ACTIVE"));

        verify(productService).updateProduct(
                1L,
                new ProductUpdateRequest("Latte")
        );
    }

    @Test
    void shouldUpdateProductStatusForAdmin() throws Exception {
        // Arrange
        ProductResponse product = new ProductResponse(
                1L,
                1L,
                "Espresso",
                ProductStatus.ACTIVE
        );

        when(securityContext.isAuthenticated()).thenReturn(true);
        when(securityContext.hasRole(Role.ADMIN)).thenReturn(true);
        when(productService.updateStatus(
                1L,
                new ProductStatusUpdateRequest(ProductStatus.ACTIVE)
        )).thenReturn(product);

        // Act & Assert
        mockMvc.perform(
                        patch("/admin/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "productStatus": "ACTIVE"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("version").value(1))
                .andExpect(jsonPath("name").value("Espresso"))
                .andExpect(jsonPath("productStatus").value("ACTIVE"));

        verify(productService).updateStatus(
                1L,
                new ProductStatusUpdateRequest(ProductStatus.ACTIVE)
        );
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        // Arrange
        when(securityContext.isAuthenticated()).thenReturn(false);

        // Act & Assert
        mockMvc.perform(
                        get("/admin/products")
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
                        get("/admin/products")
                )
                .andExpect(status().isForbidden());
    }
}