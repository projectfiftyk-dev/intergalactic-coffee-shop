package com.projfiftyk.intergalacticcoffeeshopbackend.domain.product;

public enum ProductSortField {
    NAME("name");

    private final String column;

    ProductSortField(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }
}