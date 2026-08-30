package com.projfiftyk.intergalacticcoffeeshopbackend.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleProductNotFound() {}

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleOrderNotFound() {}

    @ExceptionHandler(OrderInvalidTransitionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleOrderInvalidTransition() {}
}
