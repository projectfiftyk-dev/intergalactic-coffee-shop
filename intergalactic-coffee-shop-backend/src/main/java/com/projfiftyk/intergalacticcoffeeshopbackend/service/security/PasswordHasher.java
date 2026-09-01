package com.projfiftyk.intergalacticcoffeeshopbackend.service.security;

public interface PasswordHasher {
    String hash(String password);

    boolean matches(String password, String hash);
}
