package com.projfiftyk.intergalacticcoffeeshopbackend.mapper.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.request.ProductCreateRequest;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.product.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public List<ProductResponse> map(List<Product> products) {
        return products
                .stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getVersion(),
                        product.getName(),
                        product.getProductStatus()
                ))
                .toList();
    }

    @Override
    public ProductResponse map(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getVersion(),
                product.getName(),
                product.getProductStatus()
        );
    }

    @Override
    public Product map(ProductCreateRequest productCreateRequest) {
        Product product = new Product();
        product.setName(productCreateRequest.name());
        return product;
    }
}