package com.projfiftyk.intergalacticcoffeeshopbackend.service.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.ProductNotFoundException;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.product.ProductMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.product.ProductRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductListRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<ProductResponse> listProducts(ProductListRequest request) {

        List<Product> products =
                productRepository.getProducts(
                        request.pageSize() * (request.pageNumber() - 1),
                        request.pageSize(),
                        request.sortField(),
                        request.direction()
                );

        return mapper.map(products);
    }

    @Override
    public List<ProductResponse> listActiveProducts(ProductListRequest request) {
        List<Product> products =
                productRepository.getProducts(
                        request.pageSize() * (request.pageNumber() - 1),
                        request.pageSize(),
                        request.sortField(),
                        request.direction(),
                        List.of(ProductStatus.ACTIVE)
                );

        return mapper.map(products);
    };


    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.getProduct(id);
        if (product == null)
            throw new ProductNotFoundException(id);
        return mapper.map(product);
    }

    @Override
    public ProductResponse getActiveProduct(Long id) {
        Product product = productRepository.getProduct(id);
        if (product == null ||
            product.getProductStatus() != ProductStatus.ACTIVE)
            throw new ProductNotFoundException(id);
        return mapper.map(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request)
    {
        Product product = productRepository.getProduct(id);
        if (product == null)
            throw new ProductNotFoundException((id));

        product.setName(request.name());
        product.setVersion(product.getVersion() + 1);
        Product updated = productRepository.updateProduct(id, product);
        return mapper.map(updated);
    }

    @Override
    public ProductResponse updateStatus(Long id, ProductStatusUpdateRequest productStatus)
    {
        Product product = productRepository.getProduct(id);
        if (product == null)
            throw new ProductNotFoundException(id);

        product.setProductStatus(productStatus.productStatus());
        Product updated = productRepository.updateProduct(id, product);
        return mapper.map(updated);
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
