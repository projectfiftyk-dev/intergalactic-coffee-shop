package com.projfiftyk.intergalacticcoffeeshopbackend.repository.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> getProducts();

    List<Product> getProducts(int offset, int limit, ProductSortField sortField, SortDirection sortDirection);

    Product getProduct(Long id);

    Product updateProduct(Long id, Product product);

    Product createProduct(Product product);
}
