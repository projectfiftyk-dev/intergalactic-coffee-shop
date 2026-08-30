package com.projfiftyk.intergalacticcoffeeshopbackend.error;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;

public class OrderInvalidTransitionException extends RuntimeException {

    public OrderInvalidTransitionException(
            OrderStatus currentStatus,
            OrderStatus requestedStatus
    ) {
        super(
                "Cannot change order status from "
                        + currentStatus
                        + " to "
                        + requestedStatus
        );
    }
}