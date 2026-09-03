package com.projfiftyk.intergalacticcoffeeshopbackend.web.product.external;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.ProductNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.product.ProductService;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.security.SessionService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductListRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.web.security.SecurityContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ExternalProductController.class
)
class ExternalProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private SecurityContext securityContext;

    @MockitoBean
    private SessionService sessionService;

    @Test
    void shouldListActiveProducts() throws Exception {
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
                )
        );

        when(productService.listActiveProducts(request))
                .thenReturn(products);

        // Act & Assert
        mockMvc.perform(
                        get("/products")
                                .param("pageNumber", "1")
                                .param("pageSize", "10")
                                .param("sortField", "NAME")
                                .param("direction", "ASC")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].version").value(1))
                .andExpect(jsonPath("$[0].name").value("Espresso"))
                .andExpect(jsonPath("$[0].productStatus").value("ACTIVE"));

        verify(productService).listActiveProducts(request);
    }

    @Test
    void shouldGetActiveProduct() throws Exception {
        // Arrange
        ProductResponse product = new ProductResponse(
                1L,
                1L,
                "Espresso",
                ProductStatus.ACTIVE
        );

        when(productService.getActiveProduct(1L))
                .thenReturn(product);

        // Act & Assert
        mockMvc.perform(
                        get("/products/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("version").value(1))
                .andExpect(jsonPath("name").value("Espresso"))
                .andExpect(jsonPath("productStatus").value("ACTIVE"));

        verify(productService).getActiveProduct(1L);
    }

    @Test
    void shouldReturnNotFoundWhenActiveProductDoesNotExist() throws Exception {
        // Arrange
        when(productService.getActiveProduct(1L))
                .thenThrow(ProductNotFoundException.class);

        // Act & Assert
        mockMvc.perform(
                        get("/products/1")
                )
                .andExpect(status().isNotFound());

        verify(productService).getActiveProduct(1L);
    }
}