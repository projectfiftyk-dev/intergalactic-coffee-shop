package com.projfiftyk.intergalacticcoffeeshopbackend.web.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.ProductNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.product.ProductService;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.security.SessionService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductListRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.web.security.SecurityContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ProductController.class
)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private SecurityContext securityContext;

    @MockitoBean
    private SessionService sessionService;

    @Test
    void shouldListProducts() throws Exception {
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
                        "Espresso",
                        ProductStatus.ACTIVE
                ),
                new ProductResponse(
                        2L,
                        "Cappuccino",
                        ProductStatus.DEPRECATED
                )
        );

        when(productService.listProducts(request))
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
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Espresso"))
                .andExpect(jsonPath("$[0].productStatus").value("ACTIVE"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Cappuccino"))
                .andExpect(jsonPath("$[1].productStatus").value("DEPRECATED"));

        verify(productService).listProducts(request);
    }

    @Test
    void shouldGetProduct() throws Exception {

        // Arrange
        ProductResponse product = new ProductResponse(
                1L,
                "Espresso",
                ProductStatus.ACTIVE
        );

        when(productService.getProduct(any(Long.class)))
                .thenReturn(product);

        // Act & Assert
        mockMvc.perform(
                get("/products/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("Espresso"))
                .andExpect(jsonPath("productStatus").value("ACTIVE"));

        verify(productService).getProduct(any(Long.class));
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        // Arrange
        when(productService.getProduct(1L)).thenThrow(ProductNotFoundException.class);

        // Act & Assert
        mockMvc.perform(
                get("/products/1")
        )
                .andExpect(status().isNotFound());
    }
}