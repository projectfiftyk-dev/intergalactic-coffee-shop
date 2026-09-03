package com.projfiftyk.intergalacticcoffeeshopbackend.web.product.admin;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Role;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.product.ProductService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductListRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import com.projfiftyk.intergalacticcoffeeshopbackend.web.security.RequireRole;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductService productService;

    public AdminProductController(ProductService productService)
    {
        this.productService = productService;
    }

    @GetMapping
    @RequireRole(Role.ADMIN)
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
    @RequireRole(Role.ADMIN)
    public ProductResponse getProduct(@PathVariable Long id)
    {
        return productService.getProduct(id);
    }

    @PostMapping()
    @RequireRole(Role.ADMIN)
    public ProductResponse createProduct(
            @Valid @RequestBody ProductCreateRequest request)
    {
        return productService.createProduct(request);
    }

    @PutMapping("/{id}")
    @RequireRole(Role.ADMIN)
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request
    )
    {
        return productService.updateProduct(id, request);
    }

    @PatchMapping("/{id}")
    @RequireRole(Role.ADMIN)
    public ProductResponse updateProductStatus(
            @PathVariable Long id,
            @Valid @RequestBody ProductStatusUpdateRequest request)
    {
        return productService.updateStatus(id, request);
    }
}
