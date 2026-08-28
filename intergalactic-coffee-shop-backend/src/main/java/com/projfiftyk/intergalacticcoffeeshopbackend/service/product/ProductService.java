package com.projfiftyk.intergalacticcoffeeshopbackend.service.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;

import java.util.List;
import java.util.Optional;

public interface ProductService  {
    List<Product> listProducts();

    Optional<Product> getProduct(Long id);

    Optional<Product> updateProduct(Long id, Product product);

    Optional<Product> updateStatus(Long id, ProductStatus productStatus);

    Product createProduct(Product product);
}
