package com.projfiftyk.intergalacticcoffeeshopbackend.repository.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.OrderNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setProductId(rs.getLong("product_id"));
        order.setProductName(rs.getString("product_name"));
        order.setCreatedAt(
                rs.getTimestamp("created_at").toLocalDateTime()
        );
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));

        return order;
    };
    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> getOrders()
    {
        String sql = """
                SELECT id, product_id, product_name, status, created_at
                FROM orders
                """;

        return jdbcTemplate.query(
                sql,
                orderRowMapper
        );
    }

    @Override
    public Order getOrder(Long id) {
        String sql = """
                SELECT id, product_id, product_name, status, created_at
                FROM orders
                WHERE id = ?
                """;

        List<Order> orders = jdbcTemplate
                .query(sql, orderRowMapper, id);

        return orders
                .stream()
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        String sql = """
                UPDATE orders
                SET status = ?
                WHERE id = ?
                """;

        int rowsUpdated = jdbcTemplate.update(
                sql,
                order.getStatus().name(),
                id
        );

        if (rowsUpdated == 0)
            throw new OrderNotFoundException(id);

        return getOrder(id);
    }

    @Override
    public Order createOrder(Order order) {
        String sql = """
                INSERT into orders (product_id, product_name, status, created_at)
                VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getProductId());
            ps.setString(2, order.getProductName());
            ps.setString(3, order.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(order.getCreatedAt()));

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null){
            throw new IllegalStateException("Failed to generate Product ID");
        }

        Long generatedId = key.longValue();

        order.setId(generatedId);
        return order;
    }
}
