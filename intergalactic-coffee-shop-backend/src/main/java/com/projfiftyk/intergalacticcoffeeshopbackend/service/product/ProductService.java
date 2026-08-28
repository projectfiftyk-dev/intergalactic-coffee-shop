package com.projfiftyk.intergalacticcoffeeshopbackend.service.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;

import java.util.List;

public interface ProductService  {
    List<ProductResponse> listProducts();

    ProductResponse getProduct(Long id);

    ProductResponse updateStatus(Long id, ProductStatusUpdateRequest productStatus);

    ProductResponse updateProduct(Long id, ProductUpdateRequest product);

    ProductResponse createProduct(ProductCreateRequest request);
}
