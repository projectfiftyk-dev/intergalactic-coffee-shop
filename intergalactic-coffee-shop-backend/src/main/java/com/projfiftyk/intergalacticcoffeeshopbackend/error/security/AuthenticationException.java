package com.projfiftyk.intergalacticcoffeeshopbackend.error.security;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}