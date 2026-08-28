package com.projfiftyk.intergalacticcoffeeshopbackend.repository.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Product> product = repository.getProduct(1L);

        // Assert
        assertTrue(product.isPresent());
        assertEquals("Espresso", product.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenProductDontExist(){
        // Act
        Optional<Product> product = repository.getProduct(24L);

        // Assert
        assertTrue(product.isEmpty());
    }

    @Test
    void updateShouldReturnTheUpdatedProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Latte");
        product.setProductStatus(ProductStatus.ACTIVE);

        // Act
        Optional<Product> updatedProduct = repository.updateProduct(2L, product);

        // Assert
        assertTrue(updatedProduct.isPresent());
        assertEquals("Latte", updatedProduct.get().getName());
        assertEquals(ProductStatus.ACTIVE, updatedProduct.get().getProductStatus());
        assertEquals(2L, updatedProduct.get().getId());
    }

    @Test
    void updateShouldReturnEmptyWhenProductDontExist() {
        // Arrange
        Product product = new Product();
        product.setName("Latte");
        product.setProductStatus(ProductStatus.ACTIVE);

        // Act
        Optional<Product> updatedProduct = repository.updateProduct(23L, product);

        // Assert
        assertTrue(updatedProduct.isEmpty());
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
}
