package com.projfiftyk.intergalacticcoffeeshopbackend.repository.user;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Role;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();

        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setName(rs.getString("name"));

        return user;
    };


    public JdbcUserRepository(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findByUsername(String username) {
        String userSql = """
        SELECT id, username, password, name
        FROM users
        WHERE username = ?
        """;

        List<User> users = jdbcTemplate.query(
                userSql,
                userRowMapper,
                username
        );

        if (users.isEmpty()) {
            return null;
        }

        User user = users.get(0);

        loadRoles(user);

        return user;
    }

    @Override
    public User create(User user) {
        String sql = """
        INSERT INTO users (username, password, name)
        VALUES (?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());

            return statement;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());

        return user;
    }

    @Override
    public User findById(Long id) {
        String userSql = """
        SELECT id, username, password, name
        FROM users
        WHERE id = ?
        """;

        List<User> users = jdbcTemplate.query(
                userSql,
                userRowMapper,
                id
        );

        if (users.isEmpty()) {
            return null;
        }

        User user = users.get(0);

        loadRoles(user);

        return user;
    }

    private void loadRoles(User user) {
        String roleSql = """
        SELECT r.name
        FROM roles r
        JOIN user_roles ur ON ur.role_id = r.id
        WHERE ur.user_id = ?
        """;

        Set<Role> roles = new HashSet<>(
                jdbcTemplate.query(
                        roleSql,
                        (rs, rowNum) -> Role.valueOf(rs.getString("name")),
                        user.getId()
                )
        );

        user.setRoles(roles);
    }
}
