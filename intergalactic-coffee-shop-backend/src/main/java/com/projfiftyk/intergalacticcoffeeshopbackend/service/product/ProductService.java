package com.projfiftyk.intergalacticcoffeeshopbackend.service.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductListRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductStatusUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductUpdateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;

import java.util.List;

public interface ProductService  {
    List<ProductResponse> listProducts();

    List<ProductResponse> listProducts(ProductListRequest request);

    List<ProductResponse> listActiveProducts(ProductListRequest request);

    ProductResponse getProduct(Long id);

    ProductResponse getActiveProduct(Long id);

    ProductResponse updateStatus(Long id, ProductStatusUpdateRequest productStatus);

    ProductResponse updateProduct(Long id, ProductUpdateRequest product);

    ProductResponse createProduct(ProductCreateRequest request);
}
