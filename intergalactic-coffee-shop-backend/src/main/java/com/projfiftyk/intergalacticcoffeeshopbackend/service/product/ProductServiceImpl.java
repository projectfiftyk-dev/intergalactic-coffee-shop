package com.projfiftyk.intergalacticcoffeeshopbackend.service.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> listProducts() {
        return productRepository.getProducts();
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return productRepository.getProduct(id);
    }

    @Override
    public Optional<Product> updateProduct(Long id, Product product)
    {
        Optional<Product> optionalExistingProduct = productRepository.getProduct(id);

        if (optionalExistingProduct.isEmpty())
        {
            return Optional.empty();
        }

        Product existing = optionalExistingProduct.get();
        existing.setName(product.getName());

        return productRepository.updateProduct(id, existing);
    }

    @Override
    public Optional<Product> updateStatus(Long id, ProductStatus productStatus)
    {
        Optional<Product> existingProduct = this.productRepository.getProduct(id);

        if (existingProduct.isEmpty())
        {
            return Optional.empty();
        }

        Product product = existingProduct.get();

        product.setProductStatus(productStatus);
        return productRepository.updateProduct(id, product);
    }

    @Override
    public Product createProduct(Product product)
    {
        product.setProductStatus(ProductStatus.DRAFT);
        return productRepository.createProduct(product);
    }
}
