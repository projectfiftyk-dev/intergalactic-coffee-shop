package com.projfiftyk.intergalacticcoffeeshopbackend.repository.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.OrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class JdbcOrderRepositoryIntegrationTest {

    @Autowired
    private JdbcOrderRepository repository;

    @Test
    void shouldGetOrders() {
        // Act
        List<Order> orders = repository.getOrders();

        // Assert
        assertEquals(2, orders.size());

        Order firstOrder = orders.get(0);

        assertEquals(1L, firstOrder.getId());
        assertEquals(OrderStatus.CREATED, firstOrder.getStatus());
        assertNotNull(firstOrder.getCreatedAt());

        assertNotNull(firstOrder.getOrderItems());
        assertEquals(1, firstOrder.getOrderItems().size());

        assertEquals(
                "Espresso",
                firstOrder.getOrderItems()
                        .get(0)
                        .getProductName()
        );

        Order secondOrder = orders.get(1);

        assertEquals(2L, secondOrder.getId());
        assertEquals(OrderStatus.PREPARING, secondOrder.getStatus());

        assertNotNull(secondOrder.getOrderItems());
        assertEquals(1, secondOrder.getOrderItems().size());

        assertEquals(
                "Cappuccino",
                secondOrder.getOrderItems()
                        .get(0)
                        .getProductName()
        );
    }

    @Test
    void shouldGetOrder() {
        // Act
        Order order = repository.getOrder(1L);

        // Assert
        assertNotNull(order);

        assertEquals(1L, order.getId());
        assertEquals(OrderStatus.CREATED, order.getStatus());

        assertNotNull(order.getCreatedAt());

        assertNotNull(order.getOrderItems());
        assertEquals(1, order.getOrderItems().size());

        assertEquals(
                "Espresso",
                order.getOrderItems()
                        .get(0)
                        .getProductName()
        );
    }

    @Test
    void shouldThrowWhenOrderDoesNotExist() {
        // Act & Assert
        assertThrows(
                OrderNotFoundException.class,
                () -> repository.getOrder(999L)
        );
    }

    @Test
    void shouldReturnUpdatedOrder() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.DELIVERED);

        // Act
        Order updated = repository.updateOrder(1L, order);

        // Assert
        assertNotNull(updated);

        assertEquals(1L, updated.getId());
        assertEquals(OrderStatus.DELIVERED, updated.getStatus());

        assertNotNull(updated.getOrderItems());
        assertEquals(1, updated.getOrderItems().size());

        assertEquals(
                "Espresso",
                updated.getOrderItems()
                        .get(0)
                        .getProductName()
        );
    }

    @Test
    void shouldThrowWhenOrderDoesNotExistOnUpdate() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.DELIVERED);

        // Act & Assert
        assertThrows(
                OrderNotFoundException.class,
                () -> repository.updateOrder(999L, order)
        );
    }

    @Test
    void shouldAddNewOrder() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        // Act
        Order newOrder = repository.createOrder(order);

        // Assert
        assertNotNull(newOrder);

        // Don't rely on exactly 3L unless your test data guarantees it.
        assertNotNull(newOrder.getId());

        assertEquals(
                OrderStatus.CREATED,
                newOrder.getStatus()
        );

        assertNotNull(newOrder.getCreatedAt());
    }
}