package com.projfiftyk.intergalacticcoffeeshopbackend.web.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.service.order.OrderService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request.OrderCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.request.OrderUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.order.response.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    @GetMapping
    List<OrderResponse> listOrders() {
        return orderService.listOrders();
    }

    @GetMapping("/{id}")
    OrderResponse getOrder(@PathVariable Long id)
    {
        return orderService.getOrder(id);
    }

    @PatchMapping("/{id}")
    OrderResponse updateOrder(
            @PathVariable Long id,
            @Valid  @RequestBody OrderUpdateRequest request)
    {
        return  orderService.updateOrder(id, request);
    }

    @PostMapping()
    OrderResponse createOrder(@Valid @RequestBody List<OrderCreateRequest> requests)
    {
        return orderService.createOrder(requests);
    }

}
