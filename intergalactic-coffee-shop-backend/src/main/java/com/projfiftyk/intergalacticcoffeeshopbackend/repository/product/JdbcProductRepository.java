package com.projfiftyk.intergalacticcoffeeshopbackend.repository.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.SortDirection;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductSortField;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.ProductNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Repository
public class JdbcProductRepository implements ProductRepository{
    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setProductStatus(
                ProductStatus.valueOf(rs.getString("product_status"))
        );
        product.setVersion(rs.getLong("product_version"));

        return product;
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductRepository(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> getProducts() {
        String sql = """
            SELECT id, name, product_status, product_version
            FROM products
            """;

        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    public List<Product> getProducts(
            int offset,
            int limit,
            ProductSortField sortField,
            SortDirection sortDirection) {

        String sql = """
        SELECT id, name, product_status, product_version
        FROM products
        ORDER BY
        """ + sortField.getColumn() + " " + sortDirection.name() + " " + """
        LIMIT ? OFFSET ?
        """;

        return jdbcTemplate.query(
                sql,
                productRowMapper,
                limit,
                offset
        );
    }

    @Override
    public List<Product> getProducts(
            int offset,
            int limit,
            ProductSortField sortField,
            SortDirection sortDirection,
            List<ProductStatus> productStatuses
    ) {
        String statusPlaceholders = String.join(
                ", ",
                Collections.nCopies(productStatuses.size(), "?")
        );

        String sql = """
            SELECT id, name, product_status, product_version
            FROM products
            WHERE product_status IN (%s)
            ORDER BY %s %s
            LIMIT ? OFFSET ?
            """.formatted(
                statusPlaceholders,
                sortField.getColumn(),
                sortDirection.name()
        );

        List<Object> params = new ArrayList<>();

        for (ProductStatus status : productStatuses) {
            params.add(status.name());
        }

        params.add(limit);
        params.add(offset);

        return jdbcTemplate.query(
                sql,
                productRowMapper,
                params.toArray(new Object[0])
        );
    }

    @Override
    public Product getProduct(Long id) {
        String sql = """
            SELECT id, name, product_status, product_version
            FROM products
            WHERE id = ?
            """;

        List<Product> products = jdbcTemplate.query(
                sql, productRowMapper, id
        );

        if (products.isEmpty())
            return null;

        return products.get(0);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        String sql = """
            UPDATE products
            SET name = ?, product_status = ?, product_version = ?
            WHERE id = ?
            """;

        int rowsUpdated = jdbcTemplate.update(
                sql,
                product.getName(),
                product.getProductStatus().name(),
                product.getVersion(),
                id
        );

        if (rowsUpdated == 0) {
            return null;
        }

        return getProduct(id);
    }

    @Override
    public Product createProduct(Product product)
    {
        String sql = """
                INSERT into products (name, product_status)
                VALUES (?, ?)
                """;


        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setString(2, product.getProductStatus().name());

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null){
            throw new IllegalStateException("Failed to generate Product ID");
        }

        Long generatedId = key.longValue();

        product.setId(generatedId);

        return product;
    }


}
