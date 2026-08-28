package com.projfiftyk.intergalacticcoffeeshopbackend.service.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.response.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService  {
    List<ProductResponse> listProducts();

    ProductResponse getProduct(Long id);

    ProductResponse updateStatus(Long id, ProductStatusUpdateRequest productStatus);

    ProductResponse updateProduct(Long id, ProductUpdateRequest product);

    ProductResponse createProduct(ProductCreateRequest request);
}
