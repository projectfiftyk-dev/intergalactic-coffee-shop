package com.projfiftyk.intergalacticcoffeeshopbackend.repository.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {

    private final List<Product> inMemoryProducts = new ArrayList<>();

    public void addProduct(Product product)
    {
        inMemoryProducts.add(product);
    }

    @Override
    public List<Product> getProducts() {
        return inMemoryProducts;
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return  inMemoryProducts
                .stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Product> updateProduct(Long id, Product product) {
        for (int i = 0; i < inMemoryProducts.size(); i++) {
            if (inMemoryProducts.get(i).getId().equals(id)) {
                inMemoryProducts.set(i, product);
                return Optional.of(product);
            }
        }

        return Optional.empty();
    }

    @Override
    public Product createProduct(Product product) {
        Long newId = 1L;

        if (!inMemoryProducts.isEmpty()) {
            Product lastProduct = inMemoryProducts.get(inMemoryProducts.size() - 1);
            newId = lastProduct.getId() + 1;
        }

        product.setId(newId);
        inMemoryProducts.add(product);

        return product;
    }
}