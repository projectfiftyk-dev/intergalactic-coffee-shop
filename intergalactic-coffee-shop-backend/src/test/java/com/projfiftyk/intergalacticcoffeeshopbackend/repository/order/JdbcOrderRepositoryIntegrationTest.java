package com.projfiftyk.intergalacticcoffeeshopbackend.repository.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.OrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    }

    @Test
    void shouldGetOrder() {
        Order order = repository.getOrder(1L);

        // Assert
        assertNotNull(order);
        assertEquals("Espresso", order.getProductName());
    }

    @Test
    void shouldThrowWhenOrderDontExist() {
        // Assert
        assertThrows(
                OrderNotFoundException.class,
                () -> repository.getOrder(5L)
        );
    }

    @Test
    void shouldReturnOnUpdate() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.DELIVERED);

        // Act
        Order update = repository.updateOrder(1L, order);

        // Arrange
        assertNotNull(update);
        assertEquals(1L, update.getId());
        assertEquals("Espresso", update.getProductName());
        assertEquals(OrderStatus.DELIVERED, update.getStatus());
    }

    @Test
    void shouldThrowWhenDontExistOnUpdate() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.DELIVERED);

        // Act & Assert
        assertThrows(
                OrderNotFoundException.class,
                () -> repository.updateOrder(5L, order)
        );
    }

    @Test
    void shouldAddNewOrder() {
        // Arrange
        Order order = new Order();
        order.setProductId(1L);
        order.setProductName("Espresso");
        order.setStatus(OrderStatus.DELIVERED);

        // Act
        Order newOrder = repository.createOrder(order);

        // Assert
        assertNotNull(newOrder);
        assertEquals(3L, newOrder.getId());
        assertEquals("Espresso", newOrder.getProductName());
    }
}
