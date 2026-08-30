package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMapperImplTest {
    private final OrderMapper mapper = new OrderMapperImpl();

    @Test
    void shouldMapToList()
    {
        // Arrange
        Order espresso = new Order();
        espresso.setId(1L);
        espresso.setProductId(1L);
        espresso.setProductName("Espresso");
        espresso.setStatus(OrderStatus.CREATED);
        espresso.setCreatedAt(
                LocalDateTime.of(2026, 8, 28, 10, 0)
        );

        Order cappuccino = new Order();
        cappuccino.setId(2L);
        cappuccino.setProductId(2L);
        cappuccino.setProductName("Cappuccino");
        cappuccino.setStatus(OrderStatus.PREPARING);
        cappuccino.setCreatedAt(
                LocalDateTime.of(2026, 8, 28, 10, 30)
        );

        List<Order> orders = List.of(
                espresso,
                cappuccino
        );

        // Act
        List<OrderResponse> result = mapper.map(orders);

        // Assert
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).id());
        assertEquals("Espresso", result.get(0).productName());
        assertEquals(OrderStatus.CREATED, result.get(0).status());
        assertEquals(
                LocalDateTime.of(2026, 8, 28, 10, 0),
                result.get(0).createdAt()
        );

        assertEquals(2L, result.get(1).id());
        assertEquals("Cappuccino", result.get(1).productName());
        assertEquals(OrderStatus.PREPARING, result.get(1).status());
        assertEquals(
                LocalDateTime.of(2026, 8, 28, 10, 30),
                result.get(1).createdAt()
        );
    }

    @Test
    void shouldMapToOrderRespone() {
        // Arrange
        Order espresso = new Order();
        espresso.setId(1L);
        espresso.setProductId(1L);
        espresso.setProductName("Espresso");
        espresso.setStatus(OrderStatus.CREATED);
        espresso.setCreatedAt(
                LocalDateTime.of(2026, 8, 28, 10, 0)
        );

        // Act
        OrderResponse mapped = mapper.map(espresso);

        // Arrange
        assertEquals(1L, mapped.id());
        assertEquals("Espresso", mapped.productName());
        assertEquals(OrderStatus.CREATED, mapped.status());
    }
}
