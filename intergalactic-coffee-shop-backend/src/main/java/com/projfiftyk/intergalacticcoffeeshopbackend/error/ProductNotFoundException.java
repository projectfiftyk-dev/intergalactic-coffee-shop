package com.projfiftyk.intergalacticcoffeeshopbackend.error;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product with id " + id + " was not found");
    }

    public ProductNotFoundException() {
        super("Product not found");
    }
}
