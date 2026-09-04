package com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion;

public enum PromotionSortField {
    NAME("name"),
    CREATED_AT("created_at"),
    START_DATE("start_date"),
    END_DATE("end_date");

    private final String column;

    PromotionSortField(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }

}
