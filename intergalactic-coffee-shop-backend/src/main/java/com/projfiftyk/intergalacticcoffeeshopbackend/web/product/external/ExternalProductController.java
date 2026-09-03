package com.projfiftyk.intergalacticcoffeeshopbackend.web.product.external;


import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.product.ProductService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductListRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ExternalProductController {
    private final ProductService productService;

    public ExternalProductController(
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

        return productService.listActiveProducts(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id)
    {
        return productService.getActiveProduct(id);
    }

}
