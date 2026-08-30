package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderItem;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderItemResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMapperImplTest {

    private final OrderMapper mapper = new OrderMapperImpl();

    @Test
    void shouldMapToList() {
        // Arrange
        OrderItem espressoItem = new OrderItem();
        espressoItem.setId(1L);
        espressoItem.setOrderId(1L);
        espressoItem.setProductId(1L);
        espressoItem.setProductName("Espresso");

        Order espresso = new Order();
        espresso.setId(1L);
        espresso.setStatus(OrderStatus.CREATED);
        espresso.setCreatedAt(
                LocalDateTime.of(2026, 8, 28, 10, 0)
        );
        espresso.setOrderItems(List.of(espressoItem));

        OrderItem cappuccinoItem = new OrderItem();
        cappuccinoItem.setId(2L);
        cappuccinoItem.setOrderId(2L);
        cappuccinoItem.setProductId(2L);
        cappuccinoItem.setProductName("Cappuccino");

        Order cappuccino = new Order();
        cappuccino.setId(2L);
        cappuccino.setStatus(OrderStatus.PREPARING);
        cappuccino.setCreatedAt(
                LocalDateTime.of(2026, 8, 28, 10, 30)
        );
        cappuccino.setOrderItems(List.of(cappuccinoItem));

        List<Order> orders = List.of(
                espresso,
                cappuccino
        );

        // Act
        List<OrderResponse> result = mapper.map(orders);

        // Assert
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).id());
        assertEquals(OrderStatus.CREATED, result.get(0).status());
        assertEquals(
                LocalDateTime.of(2026, 8, 28, 10, 0),
                result.get(0).createdAt()
        );

        assertEquals(1, result.get(0).orderItems().size());

        OrderItemResponse espressoResponse =
                result.get(0).orderItems().get(0);

        assertEquals(1L, espressoResponse.id());
        assertEquals(1L, espressoResponse.orderId());
        assertEquals(1L, espressoResponse.productId());
        assertEquals("Espresso", espressoResponse.productName());

        assertEquals(2L, result.get(1).id());
        assertEquals(OrderStatus.PREPARING, result.get(1).status());
        assertEquals(
                LocalDateTime.of(2026, 8, 28, 10, 30),
                result.get(1).createdAt()
        );

        assertEquals(1, result.get(1).orderItems().size());

        OrderItemResponse cappuccinoResponse =
                result.get(1).orderItems().get(0);

        assertEquals(2L, cappuccinoResponse.id());
        assertEquals(2L, cappuccinoResponse.orderId());
        assertEquals(2L, cappuccinoResponse.productId());
        assertEquals("Cappuccino", cappuccinoResponse.productName());
    }

    @Test
    void shouldMapToOrderResponse() {
        // Arrange
        OrderItem espressoItem = new OrderItem();
        espressoItem.setId(1L);
        espressoItem.setOrderId(1L);
        espressoItem.setProductId(1L);
        espressoItem.setProductName("Espresso");

        Order espresso = new Order();
        espresso.setId(1L);
        espresso.setStatus(OrderStatus.CREATED);
        espresso.setCreatedAt(
                LocalDateTime.of(2026, 8, 28, 10, 0)
        );
        espresso.setOrderItems(List.of(espressoItem));

        // Act
        OrderResponse mapped = mapper.map(espresso);

        // Assert
        assertEquals(1L, mapped.id());
        assertEquals(OrderStatus.CREATED, mapped.status());
        assertEquals(
                LocalDateTime.of(2026, 8, 28, 10, 0),
                mapped.createdAt()
        );

        assertEquals(1, mapped.orderItems().size());

        OrderItemResponse item = mapped.orderItems().get(0);

        assertEquals(1L, item.id());
        assertEquals(1L, item.orderId());
        assertEquals(1L, item.productId());
        assertEquals("Espresso", item.productName());
    }
}