package com.projfiftyk.intergalacticcoffeeshopbackend.service.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderItem;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.OrderInvalidTransitionException;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.OrderNotFoundException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

        service = new OrderServiceImpl(
                repository,
                mapper,
                productService
        );
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
                        OrderStatus.CREATED,
                        LocalDateTime.of(2026, 8, 28, 10, 0),
                        List.of()
                ),
                new OrderResponse(
                        2L,
                        OrderStatus.CREATED,
                        LocalDateTime.of(2026, 8, 28, 11, 0),
                        List.of()
                )
        );

        when(repository.getOrders()).thenReturn(orders);
        when(mapper.map(anyList())).thenReturn(mappedOrders);

        // Act
        List<OrderResponse> result = service.listOrders();

        // Assert
        assertEquals(2, result.size());
        assertEquals(mappedOrders, result);
    }

    @Test
    void shouldReturnOrderOnGet() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.now();

        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setOrderId(1L);
        item.setProductId(1L);
        item.setProductName("Espresso");

        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(dateTime);
        order.setOrderItems(List.of(item));

        OrderResponse mappedOrder = new OrderResponse(
                1L,
                OrderStatus.CREATED,
                dateTime,
                List.of()
        );

        when(repository.getOrder(1L)).thenReturn(order);
        when(mapper.map(any(Order.class))).thenReturn(mappedOrder);

        // Act
        OrderResponse result = service.getOrder(1L);

        // Assert
        assertNotNull(result);
        assertEquals(mappedOrder, result);
        assertEquals(OrderStatus.CREATED, result.status());
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
        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setProductName("Espresso");

        Order order = new Order();
        order.setId(1L);
        order.setStatus(currentStatus);
        order.setOrderItems(List.of(item));

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setStatus(newStatus);
        updatedOrder.setOrderItems(List.of(item));

        OrderResponse mappedOrder = new OrderResponse(
                1L,
                newStatus,
                order.getCreatedAt(),
                List.of()
        );

        OrderUpdateRequest request =
                new OrderUpdateRequest(newStatus);

        when(repository.getOrder(1L))
                .thenReturn(order);

        when(repository.updateOrder(1L, order))
                .thenReturn(updatedOrder);

        when(mapper.map(updatedOrder))
                .thenReturn(mappedOrder);

        // Act
        OrderResponse result =
                service.updateOrder(1L, request);

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

        when(repository.getOrder(1L))
                .thenReturn(order);

        // Act & Assert
        OrderInvalidTransitionException exception =
                assertThrows(
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
                1L,
                "Espresso",
                ProductStatus.ACTIVE
        );

        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setOrderId(1L);
        item.setProductId(1L);
        item.setProductName("Espresso");

        Order createdOrder = new Order();
        createdOrder.setId(1L);
        createdOrder.setStatus(OrderStatus.CREATED);
        createdOrder.setCreatedAt(
                LocalDateTime.of(2026, 8, 30, 15, 0)
        );
        createdOrder.setOrderItems(new ArrayList<>());

        OrderResponse mappedOrder = new OrderResponse(
                1L,
                OrderStatus.CREATED,
                createdOrder.getCreatedAt(),
                List.of()
        );

        when(productService.getProduct(1L))
                .thenReturn(product);

        when(repository.createOrder(any(Order.class)))
                .thenReturn(createdOrder);

        when(repository.addOrderItem(
                eq(1L),
                any(OrderItem.class)
        )).thenReturn(item);

        when(mapper.map(any(Order.class)))
                .thenReturn(mappedOrder);

        // Act
        OrderResponse result =
                service.createOrder(List.of(request));

        // Assert
        assertNotNull(result);
        assertEquals(mappedOrder, result);
        assertEquals(OrderStatus.CREATED, result.status());
    }

    @Test
    void shouldThrowWhenCreatingOrderWithNonExistingProduct() {
        // Arrange
        OrderCreateRequest request =
                new OrderCreateRequest(99L, 1);

        when(productService.getProduct(99L))
                .thenThrow(new ProductNotFoundException(99L));

        // Act & Assert
        ProductNotFoundException exception =
                assertThrows(
                        ProductNotFoundException.class,
                        () -> service.createOrder(List.of(request))
                );

        assertEquals(
                "Product with id 99 was not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenGettingNonExistingOrder() {
        // Arrange
        Long orderId = 999L;

        when(repository.getOrder(orderId))
                .thenThrow(new OrderNotFoundException(orderId));

        // Act & Assert
        OrderNotFoundException exception =
                assertThrows(
                        OrderNotFoundException.class,
                        () -> service.getOrder(orderId)
                );

        assertEquals(
                "Order with id 999 was not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingOrder() {
        // Arrange
        Long orderId = 999L;

        OrderUpdateRequest request =
                new OrderUpdateRequest(OrderStatus.PAID);

        when(repository.getOrder(orderId))
                .thenThrow(new OrderNotFoundException(orderId));

        // Act & Assert
        OrderNotFoundException exception =
                assertThrows(
                        OrderNotFoundException.class,
                        () -> service.updateOrder(orderId, request)
                );

        assertEquals(
                "Order with id 999 was not found",
                exception.getMessage()
        );
    }
}