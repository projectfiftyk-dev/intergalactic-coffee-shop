package com.projfiftyk.intergalacticcoffeeshopbackend.repository.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcSessionRepository implements SessionRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Session> sessionRowMapper = (rs, rowNum) -> {
        Session session = new Session();

        session.setId(rs.getString("id"));
        session.setUserId(rs.getLong("user_id"));
        session.setExpiresAt(
                rs.getTimestamp("expires_at").toLocalDateTime()
        );

        return session;
    };

    public JdbcSessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Session create(Session session) {
        String sql = """
                INSERT INTO sessions (id, user_id, expires_at)
                VALUES (?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                session.getId(),
                session.getUserId(),
                session.getExpiresAt()
        );

        return session;
    }

    @Override
    public Session findById(String id) {
        String sql = """
                SELECT id, user_id, expires_at
                FROM sessions
                WHERE id = ?
                """;

        List<Session> sessions = jdbcTemplate.query(
                sql,
                sessionRowMapper,
                id
        );

        if (sessions.isEmpty()) {
            return null;
        }

        return sessions.get(0);
    }

    @Override
    public void deleteById(String id) {
        String sql = """
                DELETE FROM sessions
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }
}