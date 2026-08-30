package com.projfiftyk.intergalacticcoffeeshopbackend.domain.order;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItem> orderItems;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public OrderStatus getStatus() { return status; }

    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getOrderItems() { return orderItems; }

    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems;}
}
