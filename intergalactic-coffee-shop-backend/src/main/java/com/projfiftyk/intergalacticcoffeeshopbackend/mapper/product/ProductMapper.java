package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.response.ProductResponse;

import java.util.List;

public interface ProductMapper {
    List<ProductResponse> map(List<Product> products);

    ProductResponse map(Product product);

    Product map(ProductCreateRequest productCreateRequest);

    Product map(ProductUpdateRequest productUpdateRequest);
}
