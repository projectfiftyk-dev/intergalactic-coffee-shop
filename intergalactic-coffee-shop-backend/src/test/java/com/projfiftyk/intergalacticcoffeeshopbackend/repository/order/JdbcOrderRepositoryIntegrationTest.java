package com.projfiftyk.intergalacticcoffeeshopbackend.repository.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderItem;
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
    void shouldCreateOrder() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        // Act
        Order created = repository.createOrder(order);

        // Assert
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(OrderStatus.CREATED, created.getStatus());
        assertNotNull(created.getCreatedAt());
    }

    @Test
    void shouldAddOrderItem() {
        // Arrange
        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setProductName("Espresso");

        // Act
        OrderItem createdItem = repository.addOrderItem(1L, item);

        // Assert
        assertNotNull(createdItem);
        assertNotNull(createdItem.getId());

        assertEquals(1L, createdItem.getOrderId());
        assertEquals(1L, createdItem.getProductId());
        assertEquals("Espresso", createdItem.getProductName());
    }

    @Test
    void shouldCreateOrderWithOrderItem() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        // Act
        Order createdOrder = repository.createOrder(order);

        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setProductName("Espresso");

        OrderItem createdItem = repository.addOrderItem(
                createdOrder.getId(),
                item
        );

        // Assert
        assertNotNull(createdOrder.getId());
        assertNotNull(createdItem.getId());

        assertEquals(
                createdOrder.getId(),
                createdItem.getOrderId()
        );

        // Verify the relationship by loading the order again
        Order loadedOrder = repository.getOrder(createdOrder.getId());

        assertEquals(1, loadedOrder.getOrderItems().size());

        OrderItem loadedItem = loadedOrder
                .getOrderItems()
                .get(0);

        assertEquals(createdItem.getId(), loadedItem.getId());
        assertEquals(1L, loadedItem.getProductId());
        assertEquals("Espresso", loadedItem.getProductName());
    }
}