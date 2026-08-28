package com.projfiftyk.intergalacticcoffeeshopbackend.repository.product;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.Product;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.product.ProductStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLTransactionRollbackException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcProductRepository implements ProductRepository{
    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setProductStatus(
                ProductStatus.valueOf(rs.getString("product_status"))
        );

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
                SELECT id, name, product_status
                FROM products
                """;

        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        String sql = """
                SELECT id, name, product_status
                FROM products
                WHERE id = ?
                """;

        List<Product> products = jdbcTemplate.query(
                sql, productRowMapper, id
        );

        return products.stream().findFirst();
    }

    @Override
    public Optional<Product> updateProduct(Long id, Product product)
    {
        String sql = """
                UPDATE products
                SET name = ?, product_status = ?
                WHERE id = ?
                """;

        int rowsUpdated = jdbcTemplate.update(
                sql,
                product.getName(), product.getProductStatus().name(),
                id
        );

        if (rowsUpdated == 0)
        {
            return Optional.empty();
        }

        return  getProduct(id);
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
