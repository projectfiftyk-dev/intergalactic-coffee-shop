package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.response.ProductResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductMapperImplTest {
    private final ProductMapper mapper = new ProductMapperImpl();

    @Test
    void shouldMapToListProductResponse() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Espresso");
        product1.setProductStatus(ProductStatus.ACTIVE);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Cappuccino");
        product2.setProductStatus(ProductStatus.DEPRECATED);

        List<Product> products = List.of(product1, product2);

        // Act
        List<ProductResponse> responses = mapper.map(products);

        // Assert
        assertEquals(2, responses.size());

        ProductResponse espresso = responses.get(0);
        assertEquals(1L, espresso.id());
        assertEquals("Espresso", espresso.name());
        assertEquals(ProductStatus.ACTIVE, espresso.productStatus());

        ProductResponse cappuccino = responses.get(1);
        assertEquals(2L, cappuccino.id());
        assertEquals("Cappuccino", cappuccino.name());
        assertEquals(ProductStatus.DEPRECATED, cappuccino.productStatus());
    }

    @Test
    void shouldMapToProductResponse() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Espresso");
        product.setProductStatus(ProductStatus.ACTIVE);

        // Act
        ProductResponse response = mapper.map(product);

        // Assert
        assertEquals(1L, response.id());
        assertEquals("Espresso", response.name());
        assertEquals(ProductStatus.ACTIVE, response.productStatus());

    }

    @Test
    void shouldMapToProductFromCreateRequest() {
        // Arrange
        ProductCreateRequest request = new ProductCreateRequest("Espresso");

        // Act
        Product product = mapper.map(request);

        // Assert
        assertNull(product.getId());
        assertEquals("Espresso", product.getName());
    }

    @Test
    void shouldMapToProductFromUpdateRequest() {
        // Arrange
        ProductUpdateRequest request = new ProductUpdateRequest("Espresso");

        // Act
        Product product = mapper.map(request);

        // Assert
        assertNull(product.getId());
        assertEquals("Espresso", product.getName());
    }


}
