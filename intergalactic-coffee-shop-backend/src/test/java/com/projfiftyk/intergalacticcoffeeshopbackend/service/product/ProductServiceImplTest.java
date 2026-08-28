package com.projfiftyk.intergalacticcoffeeshopbackend.service.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.ProductNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.product.ProductMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.product.InMemoryProductRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.product.ProductRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {
    private InMemoryProductRepository repository;
    private ProductMapper mapper;
    private ProductService service;

    @BeforeEach
    void setUp() {
        // Arrange
        repository = new InMemoryProductRepository();
        mapper = mock(ProductMapper.class);

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

        service = new ProductServiceImpl(repository, mapper);
    }

    @Test
    void shouldListProducts() {
        // Arrange
        List<ProductResponse> mappedProducts = List.of(
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

        when(mapper.map(anyList())).thenReturn(mappedProducts);

        // Act
        List<ProductResponse> products = service.listProducts();

        // Assert
        assertEquals(mappedProducts, products);
    }

    @Test
    void shouldGetProduct() {
        // Arrange
        ProductResponse mappedProduct = new ProductResponse(
                1L,
                "Espresso",
                ProductStatus.ACTIVE
        );

        when(mapper.map(any(Product.class))).thenReturn(mappedProduct);

        // Act
        ProductResponse product = service.getProduct(1L);

        // Assert
        assertNotNull(product);
        assertEquals("Espresso", product.name());
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        // Act & Assert
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> service.getProduct(3L)
        );

        assertEquals(
                "Product with id 3 was not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldUpdateProduct() {
        // Arrange
        ProductUpdateRequest updateRequest = new ProductUpdateRequest(
                "Espresso"
        );
        ProductResponse mappedProduct = new ProductResponse(
                1L,
                "Espresso",
                ProductStatus.ACTIVE
        );

        when(mapper.map(any(Product.class))).thenReturn(mappedProduct);

        // Act
        ProductResponse updatedProduct = service.updateProduct(1L, updateRequest);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals("Espresso", updatedProduct.name());
    }

    @Test
    void shouldThrowOnUpdateWhenProductDontExist(){
        // Arrange
        ProductUpdateRequest updateRequest = new ProductUpdateRequest(
                "Espresso"
        );

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> service.updateProduct(5L, updateRequest)
        );

        assertEquals(
                "Product with id 5 was not found",
                exception.getMessage()
        );
    }

//    @Test
//    void shouldThrowOnUpdateWhenUpdateDBError() {
//        // Arrange
//        when(repository.updateProduct(any(), any())).thenReturn(Optional.empty());
//        ProductUpdateRequest updateRequest = new ProductUpdateRequest(
//                "Espresso"
//        );
//
//        // Act & Assert
//        assertThrows(
//                RuntimeException.class,
//                () -> service.updateProduct(1L, updateRequest)
//        );
//    }

    @Test
    void shouldUpdateProductStatus()
    {
        // Arrange
        ProductStatusUpdateRequest statusUpdateRequest = new ProductStatusUpdateRequest(ProductStatus.DEPRECATED);
        ProductResponse mappedResponse = new ProductResponse(
            1L,
            "Espresso",
            ProductStatus.DEPRECATED
        );
        when(mapper.map(any(Product.class))).thenReturn(mappedResponse);

        // Act
        ProductResponse updated = service.updateStatus(1L, statusUpdateRequest);

        // Assert
        assertNotNull(updated);
        assertEquals("Espresso", updated.name());
    }

    @Test
    void shouldThrowOnStatusUpdateWhenDontExist(){
        // Arrange
        ProductStatusUpdateRequest statusUpdateRequest = new ProductStatusUpdateRequest(ProductStatus.DEPRECATED);

        // Act & Assert
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> service.updateStatus(5L, statusUpdateRequest)
        );

        assertEquals(
                "Product with id 5 was not found",
                exception.getMessage()
        );

    }

    @Test
    void shouldCreateNewProduct() {
        // Arrange
        ProductCreateRequest request = new ProductCreateRequest("Machiato");

        Product product = new Product();
        product.setName("Machiato");

        ProductResponse mappedResponse = new ProductResponse(
                5L,
                "Machiato",
                ProductStatus.DRAFT
        );

        when(mapper.map(any(ProductCreateRequest.class)))
                .thenReturn(product);

        when(mapper.map(any(Product.class)))
                .thenReturn(mappedResponse);

        // Act
        ProductResponse response = service.createProduct(request);

        // Assert
        assertEquals("Machiato", response.name());
        assertEquals(ProductStatus.DRAFT, response.productStatus());
    }
}