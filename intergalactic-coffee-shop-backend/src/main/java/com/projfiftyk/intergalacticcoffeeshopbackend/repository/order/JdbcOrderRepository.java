package com.projfiftyk.intergalacticcoffeeshopbackend.repository.order;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.Order;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderItem;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.order.OrderStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.OrderNotFoundException;
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

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Order order = new Order();

        order.setId(rs.getLong("id"));
        order.setStatus(
                OrderStatus.valueOf(rs.getString("status"))
        );
        order.setCreatedAt(
                rs.getTimestamp("created_at").toLocalDateTime()
        );

        return order;
    };

    private final RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) -> {
        OrderItem item = new OrderItem();

        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setProductName(rs.getString("product_name"));

        return item;
    };

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> getOrders() {
        String sql = """
                SELECT id, status, created_at
                FROM orders
                """;

        List<Order> orders = jdbcTemplate.query(
                sql,
                orderRowMapper
        );

        orders.forEach(order ->
                order.setOrderItems(getOrderItems(order.getId()))
        );

        return orders;
    }

    @Override
    public Order getOrder(Long id) {
        String sql = """
                SELECT id, status, created_at
                FROM orders
                WHERE id = ?
                """;

        List<Order> orders = jdbcTemplate.query(
                        sql,
                        orderRowMapper,
                        id
                );

        if (orders.isEmpty())
            return null;

        Order order = orders.get(0);
        order.setOrderItems(getOrderItems(id));

        return order;
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

        if (rowsUpdated == 0) {
            return null;
        }

        return getOrder(id);
    }

    @Override
    public Order createOrder(Order order) {
        String sql = """
                INSERT INTO orders (status, created_at)
                VALUES (?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, order.getStatus().name());
            ps.setTimestamp(
                    2,
                    Timestamp.valueOf(order.getCreatedAt())
            );

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new IllegalStateException(
                    "Failed to generate Order ID"
            );
        }

        Long generatedId = key.longValue();

        order.setId(generatedId);

        return getOrder(generatedId);
    }

    @Override
    public OrderItem addOrderItem(Long orderId, OrderItem item) {
        String sql = """
            INSERT INTO order_items (
                order_id,
                product_id,
                product_name
            )
            VALUES (?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, orderId);
            ps.setLong(2, item.getProductId());
            ps.setString(3, item.getProductName());

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new IllegalStateException(
                    "Failed to generate Order Item ID"
            );
        }

        item.setId(key.longValue());
        item.setOrderId(orderId);

        return item;
    }

    private List<OrderItem> getOrderItems(Long orderId) {
        String sql = """
                SELECT id, order_id, product_id, product_name
                FROM order_items
                WHERE order_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                orderItemRowMapper,
                orderId
        );
    }
}