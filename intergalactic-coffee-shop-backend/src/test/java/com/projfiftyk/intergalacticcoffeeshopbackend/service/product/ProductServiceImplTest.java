package com.projfiftyk.intergalacticcoffeeshopbackend.service.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.product.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.LongToIntFunction;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceImplTest {
    private InMemoryProductRepository repository;
    private ProductService service;

    @BeforeEach
    void setUp() {
        // Arrange
        repository = new InMemoryProductRepository();

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Espresso");
        product1.setProductStatus(ProductStatus.ACTIVE);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Cappuccino");
        product2.setProductStatus(ProductStatus.DEPRECATED);

        repository.addProduct(product1);
        repository.addProduct(product2);

        service = new ProductServiceImpl(repository);
    }

    @Test
    void shouldListProducts() {
        // Act
        List<Product> products = service.listProducts();

        // Assert
        assertEquals(2, products.size());
    }

    @Test
    void shouldGetProduct() {
        // Act
        Optional<Product> product = service.getProduct(1L);

        // Assert
        assertTrue(product.isPresent());
        assertEquals("Espresso", product.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenProductDoesNotExist() {
        // Act
        Optional<Product> product = service.getProduct(3L);

        // Assert
        assertFalse(product.isPresent());
    }

    @Test
    void shouldUpdateProduct() {
        // Arrange
        Product product = new Product();
        Long id = 1L;
        product.setId(id);
        product.setName("Latte");
        product.setProductStatus(ProductStatus.ACTIVE);

        // Act
        Optional<Product> updatedProduct = service.updateProduct(id, product);

        // Assert
        assertTrue(updatedProduct.isPresent());
        assertEquals(1L, updatedProduct.get().getId());
        assertEquals("Latte", updatedProduct.get().getName());
        assertEquals(ProductStatus.ACTIVE, updatedProduct.get().getProductStatus());

    }

    @Test
    void shouldReturnEmptyWhenProductDoesNotExistOnUpdate(){
        // Arrange
        Product product = new Product();
        Long id = 23L;
        product.setId(id);
        product.setName("Latte");
        product.setProductStatus(ProductStatus.ACTIVE);

        // Act
        Optional<Product> updatedProduct = service.updateProduct(id, product);

        // Assert
        assertTrue(updatedProduct.isEmpty());
    }

    @Test
    void shouldUpdateProductStatus()
    {
        // Act
        Optional<Product> updatedProduct = service.updateStatus(1L, ProductStatus.DEPRECATED);

        // Assert
        assertTrue(updatedProduct.isPresent());
        assertEquals(1L, updatedProduct.get().getId());
        assertEquals(ProductStatus.DEPRECATED, updatedProduct.get().getProductStatus());
    }

    @Test
    void shouldReturnEmptyProductDoesNotExistOnStatusChange(){
        // Act
        Optional<Product> updatedProduct = service.updateStatus(10L, ProductStatus.ACTIVE);

        // Assert
        assertTrue(updatedProduct.isEmpty());
    }

    @Test
    void shouldCreateNewProduct() {
        // Arrange
        Product newProduct = new Product();
        newProduct.setName("Machiato");
        newProduct.setProductStatus(ProductStatus.ACTIVE);

        // Act
        Product addedProduct = service.createProduct(newProduct);

        // Arrange
        assertEquals(3L, addedProduct.getId());
        assertEquals(ProductStatus.DRAFT, addedProduct.getProductStatus());
    }
}