package com.projfiftyk.intergalacticcoffeeshopbackend.web.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.product.ProductService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductListRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(
            ProductService productService)
    {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> getProducts(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "NAME") ProductSortField sortField,
            @RequestParam(defaultValue = "ASC") SortDirection direction
    ) {
        ProductListRequest request = new ProductListRequest(
                pageNumber,
                pageSize,
                sortField,
                direction
        );

        return productService.listProducts(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id)
    {
        return productService.getProduct(id);
    }

    @PostMapping()
    public ProductResponse createProduct(
            @Valid  @RequestBody ProductCreateRequest request)
    {
        return productService.createProduct(request);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request
            )
    {
        return productService.updateProduct(id, request);
    }

    @PatchMapping("/{id}")
    public ProductResponse updateProductStatus(
            @PathVariable Long id,
            @Valid @RequestBody ProductStatusUpdateRequest request)
    {
        return productService.updateStatus(id, request);
    }
}
