package com.projfiftyk.intergalacticcoffeeshopbackend.web.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.mapper.product.ProductMapper;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.product.ProductService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.response.ProductResponse;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(
            ProductService productService,
            ProductMapper productMapper)
    {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public List<ProductResponse> getProducts() {
        List<Product> products = productService.listProducts();
        return productMapper.map(products);
    }

    @GetMapping("/{id}")
    public Optional<ProductResponse> getProduct(@PathVariable Long id)
    {
        Optional<Product> product = productService.getProduct(id);
        if (product.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.of(productMapper.map(product.get()));
    }

    @PostMapping()
    public ProductResponse createProduct(@RequestBody ProductCreateRequest request)
    {
        Product product = productMapper.map(request);
        Product createdProduct = productService.createProduct(product);
        return productMapper.map(createdProduct);
    }

    @PutMapping("/{id}")
    public Optional<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request
            )
    {
        Product product = productMapper.map(request);
        Optional<Product> optionalProduct = productService.updateProduct(id, product);
        if (optionalProduct.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.of(productMapper.map(optionalProduct.get()));
    }

    @PatchMapping("/{id}")
    public Optional<ProductResponse> updateProductStatus(
            @PathVariable Long id,
            @RequestBody ProductStatusUpdateRequest request)
    {
        Optional<Product> optionalProduct = productService.updateStatus(id, request.productStatus());

        if (optionalProduct.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.of(productMapper.map(optionalProduct.get()));

    }
}
