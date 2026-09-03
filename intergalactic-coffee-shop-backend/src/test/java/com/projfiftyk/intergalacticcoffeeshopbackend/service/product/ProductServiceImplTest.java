package com.projfiftyk.intergalacticcoffeeshopbackend.service.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.ProductNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.product.ProductMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.product.ProductRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductListRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    private ProductRepository repository;
    private ProductMapper mapper;
    private ProductService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        mapper = mock(ProductMapper.class);

        service = new ProductServiceImpl(repository, mapper);
    }

    @Test
    void shouldListProducts() {
        // Arrange
        List<Product> products = List.of(
                new Product(),
                new Product()
        );

        List<ProductResponse> mappedProducts = List.of(
                new ProductResponse(
                        1L,
                        1L,
                        "Espresso",
                        ProductStatus.ACTIVE
                ),
                new ProductResponse(
                        2L,
                        1L,
                        "Cappuccino",
                        ProductStatus.DEPRECATED
                )
        );

        when(repository.getProducts()).thenReturn(products);
        when(mapper.map(anyList())).thenReturn(mappedProducts);

        // Act
        List<ProductResponse> result = service.listProducts();

        // Assert
        assertEquals(mappedProducts, result);
    }

    @Test
    void shouldGetProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Espresso");
        product.setProductStatus(ProductStatus.ACTIVE);
        product.setVersion(3L);

        ProductResponse mappedProduct = new ProductResponse(
                1L,
                3L,
                "Espresso",
                ProductStatus.ACTIVE
        );

        when(repository.getProduct(1L)).thenReturn(product);
        when(mapper.map(product)).thenReturn(mappedProduct);

        // Act
        ProductResponse result = service.getProduct(1L);

        // Assert
        assertEquals(mappedProduct, result);

        verify(repository).getProduct(1L);
        verify(mapper).map(product);
    }

    @Test
    void shouldUpdateProduct() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Espresso");
        existingProduct.setProductStatus(ProductStatus.ACTIVE);
        existingProduct.setVersion(1L);

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Latte");
        updatedProduct.setProductStatus(ProductStatus.ACTIVE);
        updatedProduct.setVersion(2L);

        ProductUpdateRequest request = new ProductUpdateRequest("Latte");

        ProductResponse mappedProduct = new ProductResponse(
                1L,
                2L,
                "Latte",
                ProductStatus.ACTIVE
        );

        when(repository.getProduct(1L)).thenReturn(existingProduct);
        when(repository.updateProduct(1L, existingProduct))
                .thenReturn(updatedProduct);
        when(mapper.map(updatedProduct)).thenReturn(mappedProduct);

        // Act
        ProductResponse result = service.updateProduct(1L, request);

        // Assert
        assertEquals(mappedProduct, result);
        assertEquals("Latte", existingProduct.getName());
        assertEquals(2L, existingProduct.getVersion());

        verify(repository).getProduct(1L);
        verify(repository).updateProduct(1L, existingProduct);
        verify(mapper).map(updatedProduct);
    }

    @Test
    void shouldListOnlyActiveProducts() {
        // Arrange
        ProductListRequest request = new ProductListRequest(
                1,
                10,
                ProductSortField.NAME,
                SortDirection.ASC
        );

        List<Product> products = List.of(
                new Product()
        );

        List<ProductResponse> mappedProducts = List.of(
                new ProductResponse(
                        1L,
                        1L,
                        "Espresso",
                        ProductStatus.ACTIVE
                )
        );

        when(repository.getProducts(
                0,
                10,
                ProductSortField.NAME,
                SortDirection.ASC,
                List.of(ProductStatus.ACTIVE)
        )).thenReturn(products);

        when(mapper.map(products)).thenReturn(mappedProducts);

        // Act
        List<ProductResponse> result = service.listActiveProducts(request);

        // Assert
        assertEquals(mappedProducts, result);

        verify(repository).getProducts(
                0,
                10,
                ProductSortField.NAME,
                SortDirection.ASC,
                List.of(ProductStatus.ACTIVE)
        );
    }

    @Test
    void shouldUpdateProductStatus() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Espresso");
        existingProduct.setProductStatus(ProductStatus.ACTIVE);
        existingProduct.setVersion(3L);

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Espresso");
        updatedProduct.setProductStatus(ProductStatus.DEPRECATED);
        updatedProduct.setVersion(3L);

        ProductStatusUpdateRequest request =
                new ProductStatusUpdateRequest(ProductStatus.DEPRECATED);

        ProductResponse mappedResponse = new ProductResponse(
                1L,
                3L,
                "Espresso",
                ProductStatus.DEPRECATED
        );

        when(repository.getProduct(1L)).thenReturn(existingProduct);
        when(repository.updateProduct(1L, existingProduct))
                .thenReturn(updatedProduct);
        when(mapper.map(updatedProduct)).thenReturn(mappedResponse);

        // Act
        ProductResponse result = service.updateStatus(1L, request);

        // Assert
        assertEquals(mappedResponse, result);
        assertEquals(
                ProductStatus.DEPRECATED,
                existingProduct.getProductStatus()
        );
        assertEquals(3L, existingProduct.getVersion());
    }

    @Test
    void shouldThrowOnStatusUpdateWhenProductDoesNotExist() {
        // Arrange
        ProductStatusUpdateRequest request =
                new ProductStatusUpdateRequest(ProductStatus.DEPRECATED);

        when(repository.getProduct(5L))
                .thenThrow(new ProductNotFoundException(5L));

        // Act & Assert
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> service.updateStatus(5L, request)
        );

        assertEquals(
                "Product with id 5 was not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateNewProduct() {
        // Arrange
        ProductCreateRequest request =
                new ProductCreateRequest("Machiato");

        Product product = new Product();
        product.setName("Machiato");

        Product createdProduct = new Product();
        createdProduct.setId(5L);
        createdProduct.setName("Machiato");
        createdProduct.setProductStatus(ProductStatus.DRAFT);

        ProductResponse mappedResponse = new ProductResponse(
                5L,
                1L,
                "Machiato",
                ProductStatus.DRAFT
        );

        when(mapper.map(request)).thenReturn(product);
        when(repository.createProduct(product)).thenReturn(createdProduct);
        when(mapper.map(createdProduct)).thenReturn(mappedResponse);

        // Act
        ProductResponse result = service.createProduct(request);

        // Assert
        assertEquals("Machiato", result.name());
        assertEquals(ProductStatus.DRAFT, result.productStatus());
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        // Arrange
        Long productId = 3L;

        when(repository.getProduct(productId))
                .thenReturn(null);

        // Act & Assert
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> service.getProduct(productId)
        );

        assertEquals(
                "Product with id 3 was not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowOnUpdateWhenProductDoesNotExist() {
        // Arrange
        Long productId = 5L;

        ProductUpdateRequest request =
                new ProductUpdateRequest("Latte");

        when(repository.getProduct(productId))
                .thenReturn(null);

        // Act & Assert
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> service.updateProduct(productId, request)
        );

        assertEquals(
                "Product with id 5 was not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldListProductsWithPagination() {
        // Arrange
        ProductListRequest request = new ProductListRequest(
                1,
                10,
                ProductSortField.NAME,
                SortDirection.ASC
        );

        List<Product> products = List.of(
                new Product(),
                new Product()
        );

        List<ProductResponse> mappedProducts = List.of(
                new ProductResponse(
                        1L,
                        1L,
                        "Cappuccino",
                        ProductStatus.ACTIVE
                ),
                new ProductResponse(
                        2L,
                        1L,
                        "Espresso",
                        ProductStatus.ACTIVE
                )
        );

        when(repository.getProducts(
                0,
                10,
                ProductSortField.NAME,
                SortDirection.ASC
        )).thenReturn(products);

        when(mapper.map(products)).thenReturn(mappedProducts);

        // Act
        List<ProductResponse> result = service.listProducts(request);

        // Assert
        assertEquals(mappedProducts, result);

        verify(repository).getProducts(
                0,
                10,
                ProductSortField.NAME,
                SortDirection.ASC
        );
    }

    @Test
    void shouldCalculateOffsetForSecondPage() {
        // Arrange
        ProductListRequest request = new ProductListRequest(
                2,
                10,
                ProductSortField.NAME,
                SortDirection.ASC
        );

        List<Product> products = List.of(new Product());

        List<ProductResponse> mappedProducts = List.of(
                new ProductResponse(
                        2L,
                        1L,
                        "Espresso",
                        ProductStatus.ACTIVE
                )
        );

        when(repository.getProducts(
                10,
                10,
                ProductSortField.NAME,
                SortDirection.ASC
        )).thenReturn(products);

        when(mapper.map(products)).thenReturn(mappedProducts);

        // Act
        List<ProductResponse> result = service.listProducts(request);

        // Assert
        assertEquals(mappedProducts, result);

        verify(repository).getProducts(
                10,
                10,
                ProductSortField.NAME,
                SortDirection.ASC
        );
    }

    @Test
    void shouldGetOnlyActiveProduct() {
        // Arrange
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Espresso");
        product.setProductStatus(ProductStatus.ACTIVE);
        product.setVersion(4L);

        ProductResponse mappedProduct = new ProductResponse(
                productId,
                4L,
                "Espresso",
                ProductStatus.ACTIVE
        );

        when(repository.getProduct(productId)).thenReturn(product);
        when(mapper.map(product)).thenReturn(mappedProduct);

        // Act
        ProductResponse result = service.getActiveProduct(productId);

        // Assert
        assertEquals(mappedProduct, result);

        verify(repository).getProduct(productId);
        verify(mapper).map(product);
    }
}