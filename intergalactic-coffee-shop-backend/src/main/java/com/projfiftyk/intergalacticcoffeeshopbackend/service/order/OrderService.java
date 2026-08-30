package com.projfiftyk.intergalacticcoffeeshopbackend.service.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request.OrderCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request.OrderUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    List<OrderResponse> listOrders();

    OrderResponse getOrder(Long id);

    OrderResponse updateOrder(Long id, OrderUpdateRequest request);

    OrderResponse createOrder(List<OrderCreateRequest> request);
}
