package com.projfiftyk.intergalacticcoffeeshopbackend.error;

public class PromotionNotFoundException extends RuntimeException {
    public PromotionNotFoundException(Long id) { super("Promotion with id " + id + " was not found"); }
}
