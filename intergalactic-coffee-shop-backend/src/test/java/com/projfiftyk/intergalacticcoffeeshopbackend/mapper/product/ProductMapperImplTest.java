package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductMapperImplTest {

    private final ProductMapper mapper = new ProductMapperImpl();

    @Test
    void shouldMapToListProductResponse() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Espresso");
        product1.setProductStatus(ProductStatus.ACTIVE);
        product1.setVersion(1L);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Cappuccino");
        product2.setProductStatus(ProductStatus.DEPRECATED);
        product2.setVersion(3L);

        List<Product> products = List.of(product1, product2);

        // Act
        List<ProductResponse> responses = mapper.map(products);

        // Assert
        assertEquals(2, responses.size());

        ProductResponse espresso = responses.get(0);
        assertEquals(1L, espresso.id());
        assertEquals("Espresso", espresso.name());
        assertEquals(ProductStatus.ACTIVE, espresso.productStatus());
        assertEquals(1, espresso.version());

        ProductResponse cappuccino = responses.get(1);
        assertEquals(2L, cappuccino.id());
        assertEquals("Cappuccino", cappuccino.name());
        assertEquals(ProductStatus.DEPRECATED, cappuccino.productStatus());
        assertEquals(3, cappuccino.version());
    }

    @Test
    void shouldMapToProductResponse() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Espresso");
        product.setProductStatus(ProductStatus.ACTIVE);
        product.setVersion(2L);

        // Act
        ProductResponse response = mapper.map(product);

        // Assert
        assertEquals(1L, response.id());
        assertEquals("Espresso", response.name());
        assertEquals(ProductStatus.ACTIVE, response.productStatus());
        assertEquals(2, response.version());
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
}