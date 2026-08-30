package com.projfiftyk.intergalacticcoffeeshopbackend.service.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.OrderInvalidTransitionException;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.order.OrderMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.order.OrderRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.product.ProductService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request.OrderCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request.OrderUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final ProductService productService;

    private final Map<OrderStatus, Set<OrderStatus>> allowedTransitions = Map.of(
            OrderStatus.CREATED, Set.of(
                    OrderStatus.PAID,
                    OrderStatus.CANCELED
            ),
            OrderStatus.PAID, Set.of(
                    OrderStatus.PREPARING
            ),
            OrderStatus.PREPARING, Set.of(
                    OrderStatus.DELIVERED
            )
    );

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderMapper mapper,
            ProductService productService)
    {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.productService = productService;
    }

    @Override
    public List<OrderResponse> listOrders() {
        List<Order> orders = orderRepository.getOrders();
        return mapper.map(orders);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.getOrder(id);
        return mapper.map(order);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderUpdateRequest request) {
        Order order = orderRepository.getOrder(id);
        validateTransition(order.getStatus(), request.status());
        order.setStatus(request.status());
        Order updated = orderRepository.updateOrder(id, order);
        return mapper.map(updated);
    }

    @Override
    public List<OrderResponse> createOrder(List<OrderCreateRequest> request) {
        List<OrderResponse> response = new ArrayList<OrderResponse>();

        for (OrderCreateRequest createRequest: request)
        {
            for (int i = 0; i < createRequest.quantity(); i++)
            {
                ProductResponse product = productService.getProduct(createRequest.productId());
                Order order = new Order();
                order.setStatus(OrderStatus.CREATED);
                order.setProductId(product.id());
                order.setProductName(product.name());
                order.setCreatedAt(LocalDateTime.now());

                Order created = orderRepository.createOrder(order);
                OrderResponse mapped = mapper.map(created);
                response.add(mapped);
            }
        }

        return response;
    }

    private void validateTransition(
            OrderStatus current,
            OrderStatus target
    ) {
        Set<OrderStatus> allowed =
                allowedTransitions.getOrDefault(current, Set.of());

        if (!allowed.contains(target)) {
            throw new OrderInvalidTransitionException(current, target);
        }
    }
}
