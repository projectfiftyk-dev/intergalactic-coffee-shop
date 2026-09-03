package com.projfiftyk.intergalacticcoffeeshopbackend.repository.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.ProductNotFoundException;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JdbcProductRepositoryIntegrationTest {

    @Autowired
    private JdbcProductRepository repository;

    @Test
    void shouldGetProducts() {
        // Act
        List<Product> products = repository.getProducts();

        // Assert
        assertEquals(2, products.size());
    }

    @Test
    void shouldGetProduct() {
        // Act
        Product product = repository.getProduct(1L);

        // Assert
        assertEquals("Espresso", product.getName());
    }

    @Test
    void updateShouldReturnTheUpdatedProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Latte");
        product.setProductStatus(ProductStatus.ACTIVE);
        // Act
        Product updatedProduct = repository.updateProduct(2L, product);

        // Assert
        assertEquals("Latte", updatedProduct.getName());
        assertEquals(ProductStatus.ACTIVE, updatedProduct.getProductStatus());
        assertEquals(2L, updatedProduct.getId());
    }

    @Test
    void createShouldReturnCreatedProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Machiato");
        product.setProductStatus(ProductStatus.ACTIVE);

        // Act
        Product createdProduct = repository.createProduct(product);

        // Assert
        assertNotNull(createdProduct.getId());
    }

    @Test
    void shouldGetProductsSortedByNameAscending() {
        // Act
        List<Product> products = repository.getProducts(
                0,
                10,
                ProductSortField.NAME,
                SortDirection.ASC
        );

        // Assert
        assertEquals(2, products.size());
        assertEquals("Cappuccino", products.get(0).getName());
        assertEquals("Espresso", products.get(1).getName());
    }

    @Test
    void shouldGetProductsSortedByNameDescending() {
        // Act
        List<Product> products = repository.getProducts(
                0,
                10,
                ProductSortField.NAME,
                SortDirection.DESC
        );

        // Assert
        assertEquals(2, products.size());
        assertEquals("Espresso", products.get(0).getName());
        assertEquals("Cappuccino", products.get(1).getName());
    }

    @Test
    void shouldGetProductsWithPagination() {
        // Act
        List<Product> products = repository.getProducts(
                0,
                1,
                ProductSortField.NAME,
                SortDirection.ASC
        );

        // Assert
        assertEquals(1, products.size());
        assertEquals("Cappuccino", products.get(0).getName());
    }

    @Test
    void shouldGetProductsWithOffset() {
        // Act
        List<Product> products = repository.getProducts(
                1,
                1,
                ProductSortField.NAME,
                SortDirection.ASC
        );

        // Assert
        assertEquals(1, products.size());
        assertEquals("Espresso", products.get(0).getName());
    }

}
