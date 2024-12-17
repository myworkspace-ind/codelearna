package mks.myworkspace.learna.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Voucher;

@Repository
public class VoucherJdbcRespository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Voucher save(Voucher voucher) {
        if (voucher.getId() == null) {
            // INSERT nếu id == null
            String sql = "INSERT INTO learna_voucher (campaign_id, name, discount_value, value_type, max_value, quantity, "
                    + "start_date, end_date, description, `condition`, created_date, modified_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int paramIndex = 1;
                ps.setLong(paramIndex++, voucher.getCampaign().getId());
                ps.setString(paramIndex++, voucher.getName());
                ps.setDouble(paramIndex++, voucher.getDiscountValue());
                ps.setString(paramIndex++, voucher.getValueType().name());
                ps.setObject(paramIndex++, voucher.getMaxValue()); // Có thể NULL
                ps.setInt(paramIndex++, voucher.getQuantity());
                ps.setTimestamp(paramIndex++, new java.sql.Timestamp(voucher.getStartDate().getTime()));
                ps.setTimestamp(paramIndex++, new java.sql.Timestamp(voucher.getEndDate().getTime()));
                ps.setString(paramIndex++, voucher.getDescription());
                ps.setString(paramIndex++, voucher.getCondition());
                return ps;
            }, keyHolder);

            Long generatedId = keyHolder.getKey().longValue();
            voucher.setId(generatedId);

        } else {
            // UPDATE nếu id != null
            String sql = "UPDATE learna_voucher SET campaign_id = ?, name = ?, discount_value = ?, value_type = ?, "
                    + "max_value = ?, quantity = ?, start_date = ?, end_date = ?, description = ?, `condition` = ?, "
                    + "modified_date = NOW() WHERE id = ?";

            int rowsAffected = jdbcTemplate.update(sql,
                    voucher.getCampaign().getId(),
                    voucher.getName(),
                    voucher.getDiscountValue(),
                    voucher.getValueType().name(),
                    voucher.getMaxValue(),
                    voucher.getQuantity(),
                    new java.sql.Timestamp(voucher.getStartDate().getTime()),
                    new java.sql.Timestamp(voucher.getEndDate().getTime()),
                    voucher.getDescription(),
                    voucher.getCondition(),
                    voucher.getId()
            );

            if (rowsAffected == 0) {
                throw new RuntimeException("Voucher not found with id: " + voucher.getId());
            }
        }

        return voucher;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM learna_voucher WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        if (rowsAffected == 0) {
            throw new RuntimeException("Voucher not found with id: " + id);
        }
    }
}
