package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;

import java.util.List;

public interface ProductMapper {
    List<ProductResponse> map(List<Product> products);

    ProductResponse map(Product product);

    Product map(ProductCreateRequest productCreateRequest);
}
