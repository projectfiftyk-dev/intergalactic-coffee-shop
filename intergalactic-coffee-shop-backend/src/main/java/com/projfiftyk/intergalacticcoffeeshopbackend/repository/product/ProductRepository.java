package com.projfiftyk.intergalacticcoffeeshopbackend.repository.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> getProducts();

    Product getProduct(Long id);

    Product updateProduct(Long id, Product product);

    Product createProduct(Product product);
}
