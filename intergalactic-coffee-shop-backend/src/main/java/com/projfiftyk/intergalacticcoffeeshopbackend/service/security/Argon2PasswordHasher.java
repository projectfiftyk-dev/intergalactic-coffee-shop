package com.projfiftyk.intergalacticcoffeeshopbackend.service.security;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class Argon2PasswordHasher implements PasswordHasher {

    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;

    private static final int MEMORY_COST = 65536;
    private static final int ITERATIONS = 3;
    private static final int PARALLELISM = 1;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String hash(String password) {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        byte[] hash = generateHash(password, salt);

        return "$argon2id"
                + "$v=19"
                + "$m=" + MEMORY_COST + ",t=" + ITERATIONS + ",p=" + PARALLELISM
                + "$" + encode(salt)
                + "$" + encode(hash);
    }

    @Override
    public boolean matches(String password, String encodedHash) {
        String[] parts = encodedHash.split("\\$");

        if (parts.length != 6) {
            return false;
        }

        int memoryCost = Integer.parseInt(parts[3].split(",")[0].substring(2));
        int iterations = Integer.parseInt(parts[3].split(",")[1].substring(2));
        int parallelism = Integer.parseInt(parts[3].split(",")[2].substring(2));

        byte[] salt = decode(parts[4]);
        byte[] expectedHash = decode(parts[5]);

        byte[] actualHash = generateHash(
                password,
                salt,
                memoryCost,
                iterations,
                parallelism,
                expectedHash.length
        );

        return MessageDigest.isEqual(actualHash, expectedHash);
    }

    private byte[] generateHash(String password, byte[] salt) {
        return generateHash(
                password,
                salt,
                MEMORY_COST,
                ITERATIONS,
                PARALLELISM,
                HASH_LENGTH
        );
    }

    private byte[] generateHash(
            String password,
            byte[] salt,
            int memoryCost,
            int iterations,
            int parallelism,
            int hashLength
    ) {
        Argon2Parameters parameters = new Argon2Parameters.Builder(
                Argon2Parameters.ARGON2_id
        )
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withSalt(salt)
                .withMemoryAsKB(memoryCost)
                .withIterations(iterations)
                .withParallelism(parallelism)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);

        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] hash = new byte[hashLength];

        generator.generateBytes(passwordBytes, hash);

        return hash;
    }

    private String encode(byte[] value) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(value);
    }

    private byte[] decode(String value) {
        return Base64.getUrlDecoder().decode(value);
    }
}