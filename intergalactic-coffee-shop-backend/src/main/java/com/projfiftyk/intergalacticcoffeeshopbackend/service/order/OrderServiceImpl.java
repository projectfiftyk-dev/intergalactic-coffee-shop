package com.projfiftyk.intergalacticcoffeeshopbackend.service.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderItem;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.OrderInvalidTransitionException;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.OrderNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.order.OrderMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.order.OrderRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.product.ProductService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request.OrderCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request.OrderUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
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
            ProductService productService
    ) {
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
        if (order == null)
            throw new OrderNotFoundException(id);

        return mapper.map(order);
    }

    @Override
    public OrderResponse updateOrder(
            Long id,
            OrderUpdateRequest request
    ) {
        Order order = orderRepository.getOrder(id);
        if (order == null)
            throw new OrderNotFoundException(id);

        validateTransition(
                order.getStatus(),
                request.status()
        );

        order.setStatus(request.status());

        Order updated = orderRepository.updateOrder(id, order);

        return mapper.map(updated);
    }

    @Transactional
    @Override
    public OrderResponse createOrder(
            List<OrderCreateRequest> requests
    ) {
        Order order = new Order();

        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        Order created = orderRepository.createOrder(order);

        for (OrderCreateRequest request : requests) {

            ProductResponse product =
                    productService.getProduct(request.productId());

            for (int i = 0; i < request.quantity(); i++) {
                OrderItem orderItem = new OrderItem();

                orderItem.setProductId(product.id());
                orderItem.setProductName(product.name());

                OrderItem inserted = orderRepository.addOrderItem(
                        created.getId(), orderItem
                );

                created.getOrderItems().add(inserted);
            }
        }

        return mapper.map(created);
    }

    private void validateTransition(
            OrderStatus current,
            OrderStatus target
    ) {
        Set<OrderStatus> allowed =
                allowedTransitions.getOrDefault(
                        current,
                        Set.of()
                );

        if (!allowed.contains(target)) {
            throw new OrderInvalidTransitionException(
                    current,
                    target
            );
        }
    }
}