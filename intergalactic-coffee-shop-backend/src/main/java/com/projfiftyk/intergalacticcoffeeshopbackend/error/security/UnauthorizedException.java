package com.projfiftyk.intergalacticcoffeeshopbackend.error.security;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}