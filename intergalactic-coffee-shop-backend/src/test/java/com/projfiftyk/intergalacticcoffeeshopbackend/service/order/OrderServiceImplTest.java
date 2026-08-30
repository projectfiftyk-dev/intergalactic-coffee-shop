package com.projfiftyk.intergalacticcoffeeshopbackend.service.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.OrderInvalidTransitionException;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.ProductNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.order.OrderMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.order.OrderRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.product.ProductService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request.OrderCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request.OrderUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.internal.matchers.Or;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {
    private OrderRepository repository;
    private OrderMapper mapper;
    private ProductService productService;
    private OrderService service;

    @BeforeEach
    void setUp() {
        repository = mock(OrderRepository.class);
        mapper = mock(OrderMapper.class);
        productService = mock(ProductService.class);
        service = new OrderServiceImpl(repository, mapper, productService);
    }

    @Test
    void shouldReturnListOfOrders() {
        // Arrange
        List<Order> orders = List.of(
                new Order(),
                new Order()
        );

        List<OrderResponse> mappedOrders = List.of(
                new OrderResponse(
                        1L,
                        "Espresso",
                        OrderStatus.CREATED,
                        LocalDateTime.of(2026, 8, 28, 10, 0)
                ),
                new OrderResponse(
                        2L,
                        "Cappuccino",
                        OrderStatus.CREATED,
                        LocalDateTime.of(2026, 8, 28, 11, 0)
                )
        );

        when(repository.getOrders()).thenReturn(orders);
        when(mapper.map(anyList())).thenReturn(mappedOrders);

        // Act
        List<OrderResponse> resulted = service.listOrders();

        // Assert
        assertEquals(2, resulted.size());
        assertEquals(mappedOrders, resulted);
    }

    @Test
    void shouldReturnOrderOnGet(){
        // Arrange
        LocalDateTime dateTime = LocalDateTime.now();

        Order order = new Order();
        order.setId(1L);
        order.setProductName("Espresso");
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(dateTime);

        OrderResponse mappedOrder = new OrderResponse(
                1L,
                "Espresso",
                OrderStatus.CREATED,
                dateTime
        );

        when(repository.getOrder(1L)).thenReturn(order);
        when(mapper.map(any(Order.class))).thenReturn(mappedOrder);

        // Act
        OrderResponse resulted = service.getOrder(1L);

        // Assert
        assertNotNull(resulted);
        assertEquals(mappedOrder, resulted);
        assertEquals("Espresso", resulted.productName());
    }

    @ParameterizedTest
    @CsvSource({
            "CREATED, PAID",
            "PAID, PREPARING",
            "PREPARING, DELIVERED"
    })
    void shouldUpdateOrderStatus(
            OrderStatus currentStatus,
            OrderStatus newStatus
    ) {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setProductName("Espresso");
        order.setStatus(currentStatus);

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setProductName("Espresso");
        updatedOrder.setStatus(newStatus);

        OrderResponse mappedOrder = new OrderResponse(
                1L,
                "Espresso",
                newStatus,
                order.getCreatedAt()
        );

        OrderUpdateRequest request = new OrderUpdateRequest(newStatus);

        when(repository.getOrder(1L)).thenReturn(order);
        when(repository.updateOrder(1L, order)).thenReturn(updatedOrder);
        when(mapper.map(updatedOrder)).thenReturn(mappedOrder);

        // Act
        OrderResponse result = service.updateOrder(1L, request);

        // Assert
        assertEquals(newStatus, result.status());
    }

    @ParameterizedTest
    @CsvSource({
            "PAID, CREATED",
            "PREPARING, CREATED",
            "PREPARING, PAID",
            "DELIVERED, CREATED",
            "DELIVERED, PAID",
            "DELIVERED, PREPARING",
            "CREATED, PREPARING",
            "CREATED, DELIVERED",
            "PAID, DELIVERED"
    })
    void shouldRejectInvalidOrderStatusTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus
    ) {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setStatus(currentStatus);

        OrderUpdateRequest request =
                new OrderUpdateRequest(newStatus);

        when(repository.getOrder(1L)).thenReturn(order);

        // Act & Assert
        OrderInvalidTransitionException exception = assertThrows(
                OrderInvalidTransitionException.class,
                () -> service.updateOrder(1L, request)
        );

        assertEquals(
                "Cannot change order status from "
                        + currentStatus
                        + " to "
                        + newStatus,
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateOrder() {
        // Arrange
        OrderCreateRequest request =
                new OrderCreateRequest(1L, 1);

        ProductResponse product = new ProductResponse(
                1L,
                "Espresso",
                ProductStatus.ACTIVE
        );

        Order createdOrder = new Order();
        createdOrder.setId(1L);
        createdOrder.setProductId(1L);
        createdOrder.setProductName("Espresso");
        createdOrder.setStatus(OrderStatus.CREATED);
        createdOrder.setCreatedAt(
                LocalDateTime.of(2026, 8, 30, 15, 0)
        );

        OrderResponse mappedOrder = new OrderResponse(
                1L,
                "Espresso",
                OrderStatus.CREATED,
                createdOrder.getCreatedAt()
        );

        when(productService.getProduct(1L))
                .thenReturn(product);

        when(repository.createOrder(any(Order.class)))
                .thenReturn(createdOrder);

        when(mapper.map(any(Order.class)))
                .thenReturn(mappedOrder);

        // Act
        List<OrderResponse> result =
                service.createOrder(List.of(request));

        // Assert
        assertEquals(1, result.size());
        assertEquals(mappedOrder, result.get(0));
        assertEquals("Espresso", result.get(0).productName());
        assertEquals(OrderStatus.CREATED, result.get(0).status());
    }

    @Test
    void shouldThrowWhenCreatingOrderWithNonExistingProduct() {
        // Arrange
        OrderCreateRequest request =
                new OrderCreateRequest(99L, 1);

        when(productService.getProduct(99L))
                .thenThrow(new ProductNotFoundException(99L));

        // Act & Assert
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> service.createOrder(List.of(request))
        );

        assertEquals(
                "Product with id 99 was not found",
                exception.getMessage()
        );
    }
}
