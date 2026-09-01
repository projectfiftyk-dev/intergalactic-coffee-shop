package com.projfiftyk.intergalacticcoffeeshopbackend.service.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Argon2PasswordHasherTest {

    private final PasswordHasher passwordHasher = new Argon2PasswordHasher();

    @Test
    void shouldHashPassword() {
        String password = "my-secret-password";

        String hash = passwordHasher.hash(password);

        assertNotNull(hash);
        assertNotEquals(password, hash);
        assertTrue(hash.startsWith("$argon2id$"));
    }

    @Test
    void shouldMatchCorrectPassword() {
        String password = "my-secret-password";

        String hash = passwordHasher.hash(password);

        assertTrue(passwordHasher.matches(password, hash));
    }

    @Test
    void shouldRejectIncorrectPassword() {
        String hash = passwordHasher.hash("my-secret-password");

        assertFalse(
                passwordHasher.matches("wrong-password", hash)
        );
    }

    @Test
    void shouldGenerateDifferentHashesForSamePassword() {
        String password = "my-secret-password";

        String hash1 = passwordHasher.hash(password);
        String hash2 = passwordHasher.hash(password);

        assertNotEquals(hash1, hash2);

        assertTrue(passwordHasher.matches(password, hash1));
        assertTrue(passwordHasher.matches(password, hash2));
    }
}