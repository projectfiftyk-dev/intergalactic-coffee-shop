package com.projfiftyk.intergalacticcoffeeshopbackend.repository.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderItem;

import java.util.List;

public interface OrderRepository {

    List<Order> getOrders();

    Order getOrder(Long id);

    Order updateOrder(Long id, Order order);

    Order createOrder(Order order);

    OrderItem addOrderItem(Long orderId, OrderItem orderItem);
}
