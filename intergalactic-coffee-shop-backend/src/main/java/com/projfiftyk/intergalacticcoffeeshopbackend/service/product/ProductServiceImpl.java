package com.projfiftyk.intergalacticcoffeeshopbackend.service.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.ProductNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.product.ProductMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.product.ProductRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductMapper mapper
    )
    {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ProductResponse> listProducts()
    {
        List<Product> products = productRepository.getProducts();
        return mapper.map(products);
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Optional<Product> optional = productRepository.getProduct(id);
        if (optional.isEmpty())
            throw new ProductNotFoundException(id);

        return mapper.map(optional.get());
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request)
    {
        Optional<Product> optionalExistingProduct = productRepository.getProduct(id);

        if (optionalExistingProduct.isEmpty())
        {
            throw new ProductNotFoundException(id);
        }

        Product existing = optionalExistingProduct.get();
        existing.setName(request.name());
        Optional<Product> optionalUpdated = productRepository.updateProduct(id, existing);
        if (optionalUpdated.isEmpty())
        {
            // TODO: handle this later
            throw new RuntimeException("DB Error");
        }

        return mapper.map(optionalUpdated.get());
    }

    @Override
    public ProductResponse updateStatus(Long id, ProductStatusUpdateRequest productStatus)
    {
        Optional<Product> existingProduct = this.productRepository.getProduct(id);

        if (existingProduct.isEmpty())
        {
            throw new ProductNotFoundException(id);
        }

        Product product = existingProduct.get();

        product.setProductStatus(productStatus.productStatus());
        Optional<Product> optionalUpdated = productRepository.updateProduct(id, product);
        if (optionalUpdated.isEmpty())
            throw new RuntimeException();

        return mapper.map(optionalUpdated.get());
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest request)
    {
        Product product = mapper.map(request);
        product.setProductStatus(ProductStatus.DRAFT);
        Product updated = productRepository.createProduct(product);
        return mapper.map(updated);
    }
}
