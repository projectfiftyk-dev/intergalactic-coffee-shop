package com.projfiftyk.intergalacticcoffeeshopbackend.error;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.security.AuthenticationException;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.security.ForbiddenException;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.security.UnauthorizedException;
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

    @ExceptionHandler(PromotionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePromotionNotFound() {}

    @ExceptionHandler(OrderInvalidTransitionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleOrderInvalidTransition() {}

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleAuthenticationException() {
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleUnauthorizedException() {
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleForbiddenException() {
    }
}
