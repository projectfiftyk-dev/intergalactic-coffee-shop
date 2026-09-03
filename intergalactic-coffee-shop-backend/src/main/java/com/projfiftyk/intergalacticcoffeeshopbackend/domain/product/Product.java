package com.projfiftyk.intergalacticcoffeeshopbackend.domain.product;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class Product {
    private Long id;
    private Long version;
    private String name;
    private ProductStatus productStatus;
    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getVersion() { return version; }

    public void setVersion(Long version) { this.version = version; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public ProductStatus getProductStatus() { return productStatus; }

    public void setProductStatus(ProductStatus productStatus) { this.productStatus = productStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
