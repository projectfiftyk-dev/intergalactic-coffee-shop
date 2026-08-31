package com.projfiftyk.intergalacticcoffeeshopbackend.repository.promotion;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.Promotion;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionRewardType;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionStatus;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.promotion.PromotionType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcPromotionRepository implements PromotionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPromotionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Promotion> getPromotions() {
        String sql = """
                SELECT id,
                       created_at,
                       start_date,
                       end_date,
                       status,
                       promotion_type,
                       occurrences,
                       minimum_value,
                       reward_type,
                       reward_value
                FROM promotions
                ORDER BY id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Promotion promotion = mapPromotion(rs);

            promotion.setProductIds(
                    getTargetProductIds(promotion.getId())
            );

            promotion.setRequiredProducts(
                    getRequiredProductIds(promotion.getId())
            );

            return promotion;
        });
    }

    @Override
    public Promotion getPromotion(Long id) {
        String sql = """
                SELECT id,
                       created_at,
                       start_date,
                       end_date,
                       status,
                       promotion_type,
                       occurrences,
                       minimum_value,
                       reward_type,
                       reward_value
                FROM promotions
                WHERE id = ?
                """;

        List<Promotion> promotions = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapPromotion(rs),
                id
        );

        if (promotions.isEmpty()) {
            return null;
        }

        Promotion promotion = promotions.get(0);

        promotion.setProductIds(
                getTargetProductIds(id)
        );

        promotion.setRequiredProducts(
                getRequiredProductIds(id)
        );

        return promotion;
    }

    @Override
    @Transactional
    public Promotion updatePromotion(
            Long id,
            Promotion promotion
    ) {
        String sql = """
                UPDATE promotions
                SET start_date = ?,
                    end_date = ?,
                    status = ?,
                    promotion_type = ?,
                    occurrences = ?,
                    minimum_value = ?,
                    reward_type = ?,
                    reward_value = ?
                WHERE id = ?
                """;

        int updatedRows = jdbcTemplate.update(
                sql,
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getStatus().name(),
                promotion.getPromotionType().name(),
                promotion.getOccurrences(),
                promotion.getMinimumValue(),
                promotion.getRewardType().name(),
                promotion.getRewardValue(),
                id
        );

        if (updatedRows == 0) {
            return null;
        }

        updateTargetProducts(
                id,
                promotion.getProductIds()
        );

        updateRequiredProducts(
                id,
                promotion.getRequiredProducts()
        );

        return getPromotion(id);
    }

    @Override
    @Transactional
    public Promotion createPromotion(Promotion promotion) {
        String sql = """
                INSERT INTO promotions (
                    created_at,
                    start_date,
                    end_date,
                    status,
                    promotion_type,
                    occurrences,
                    minimum_value,
                    reward_type,
                    reward_value
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setObject(1, promotion.getCreatedAt());
            ps.setObject(2, promotion.getStartDate());
            ps.setObject(3, promotion.getEndDate());

            ps.setString(
                    4,
                    promotion.getStatus().name()
            );

            ps.setString(
                    5,
                    promotion.getPromotionType().name()
            );

            // Nullable Integer
            ps.setObject(
                    6,
                    promotion.getOccurrences()
            );

            // Nullable BigDecimal
            ps.setObject(
                    7,
                    promotion.getMinimumValue()
            );

            ps.setString(
                    8,
                    promotion.getRewardType().name()
            );

            // BigDecimal
            ps.setObject(
                    9,
                    promotion.getRewardValue()
            );

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new IllegalStateException(
                    "Failed to generate Promotion ID"
            );
        }

        Long generatedId = key.longValue();

        promotion.setId(generatedId);

        insertTargetProducts(
                generatedId,
                promotion.getProductIds()
        );

        insertRequiredProducts(
                generatedId,
                promotion.getRequiredProducts()
        );

        return getPromotion(generatedId);
    }

    private Promotion mapPromotion(ResultSet rs)
            throws SQLException {

        Promotion promotion = new Promotion();

        promotion.setId(
                rs.getLong("id")
        );

        promotion.setCreatedAt(
                rs.getTimestamp("created_at")
                        .toLocalDateTime()
        );

        promotion.setStartDate(
                rs.getTimestamp("start_date")
                        .toLocalDateTime()
        );

        promotion.setEndDate(
                rs.getTimestamp("end_date")
                        .toLocalDateTime()
        );

        promotion.setStatus(
                PromotionStatus.valueOf(
                        rs.getString("status")
                )
        );

        promotion.setPromotionType(
                PromotionType.valueOf(
                        rs.getString("promotion_type")
                )
        );

        // Integer allows NULL
        Integer occurrences =
                (Integer) rs.getObject("occurrences");

        promotion.setOccurrences(occurrences);

        // BigDecimal for monetary values
        promotion.setMinimumValue(
                rs.getBigDecimal("minimum_value")
        );

        promotion.setRewardType(
                PromotionRewardType.valueOf(
                        rs.getString("reward_type")
                )
        );

        // BigDecimal for monetary/percentage value
        promotion.setRewardValue(
                rs.getBigDecimal("reward_value")
        );

        return promotion;
    }

    private List<Long> getTargetProductIds(
            Long promotionId
    ) {
        String sql = """
                SELECT product_id
                FROM promotion_target_products
                WHERE promotion_id = ?
                ORDER BY product_id
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        rs.getLong("product_id"),
                promotionId
        );
    }

    private List<Long> getRequiredProductIds(
            Long promotionId
    ) {
        String sql = """
                SELECT product_id
                FROM promotion_required_products
                WHERE promotion_id = ?
                ORDER BY product_id
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        rs.getLong("product_id"),
                promotionId
        );
    }

    private void updateTargetProducts(
            Long promotionId,
            List<Long> productIds
    ) {
        jdbcTemplate.update(
                """
                DELETE FROM promotion_target_products
                WHERE promotion_id = ?
                """,
                promotionId
        );

        insertTargetProducts(
                promotionId,
                productIds
        );
    }

    private void updateRequiredProducts(
            Long promotionId,
            List<Long> productIds
    ) {
        jdbcTemplate.update(
                """
                DELETE FROM promotion_required_products
                WHERE promotion_id = ?
                """,
                promotionId
        );

        insertRequiredProducts(
                promotionId,
                productIds
        );
    }

    private void insertTargetProducts(
            Long promotionId,
            List<Long> productIds
    ) {
        if (productIds == null || productIds.isEmpty()) {
            return;
        }

        String sql = """
                INSERT INTO promotion_target_products (
                    promotion_id,
                    product_id
                )
                VALUES (?, ?)
                """;

        jdbcTemplate.batchUpdate(
                sql,
                productIds,
                productIds.size(),
                (ps, productId) -> {
                    ps.setLong(1, promotionId);
                    ps.setLong(2, productId);
                }
        );
    }

    private void insertRequiredProducts(
            Long promotionId,
            List<Long> productIds
    ) {
        if (productIds == null || productIds.isEmpty()) {
            return;
        }

        String sql = """
                INSERT INTO promotion_required_products (
                    promotion_id,
                    product_id
                )
                VALUES (?, ?)
                """;

        jdbcTemplate.batchUpdate(
                sql,
                productIds,
                productIds.size(),
                (ps, productId) -> {
                    ps.setLong(1, promotionId);
                    ps.setLong(2, productId);
                }
        );
    }
}