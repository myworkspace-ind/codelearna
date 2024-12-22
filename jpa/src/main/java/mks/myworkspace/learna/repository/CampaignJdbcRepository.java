package mks.myworkspace.learna.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import mks.myworkspace.learna.entity.Campaign;

@Repository
public class CampaignJdbcRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Campaign save(Campaign campaign) {
        if (campaign.getId() == null) {
            String sql = "INSERT INTO learna_campaign (name, start_time, end_time, short_description, description, "
                    + "cover_imageUrl, status, created_dte, modified_dte) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int paramIndex = 1;
                ps.setString(paramIndex++, campaign.getName());
                ps.setTimestamp(paramIndex++, new java.sql.Timestamp(campaign.getStartTime().getTime()));
                ps.setTimestamp(paramIndex++, new java.sql.Timestamp(campaign.getEndTime().getTime()));
                ps.setString(paramIndex++, campaign.getShortDescription());
                ps.setString(paramIndex++, campaign.getDescription());
                ps.setString(paramIndex++, campaign.getCoverImageUrl());
                ps.setString(paramIndex++, campaign.getStatus() != null ? campaign.getStatus() : "ACTIVE");
                return ps;
            }, keyHolder);

            Long generatedId = keyHolder.getKey().longValue();
            campaign.setId(generatedId);
        } else {
            String sql = "UPDATE learna_campaign SET name = ?, start_time = ?, end_time = ?, short_description = ?, "
                    + "description = ?, cover_imageUrl = ?, status = ?, modified_dte = NOW() "
                    + "WHERE id = ?";

            int rowsAffected = jdbcTemplate.update(sql,
                    campaign.getName(),
                    new java.sql.Timestamp(campaign.getStartTime().getTime()),
                    new java.sql.Timestamp(campaign.getEndTime().getTime()),
                    campaign.getShortDescription(),
                    campaign.getDescription(),
                    campaign.getCoverImageUrl(),
                    campaign.getStatus(),
                    campaign.getId()
            );

            if (rowsAffected == 0) {
                throw new RuntimeException("Campaign not found with id: " + campaign.getId());
            }
        }
        return campaign;
    }

    public void deleteById(Long id) {
        String sql = "UPDATE learna_campaign SET status = 'DELETED' WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new RuntimeException("Campaign not found with id: " + id);
        }
    }
}