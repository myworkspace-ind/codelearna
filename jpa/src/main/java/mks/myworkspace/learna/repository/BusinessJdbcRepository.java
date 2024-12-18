package mks.myworkspace.learna.repository;

import mks.myworkspace.learna.entity.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class BusinessJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Business save(Business business) {
        if (business.getId() == null) {
            String sql = "INSERT INTO learna_business (name, description, address, phone, email, logo_url, establishment_date, created_dte, modified_dte) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int paramIndex = 1;
                ps.setString(paramIndex++, business.getName());
                ps.setString(paramIndex++, business.getDescription());
                ps.setString(paramIndex++, business.getAddress());
                ps.setString(paramIndex++, business.getPhone());
                ps.setString(paramIndex++, business.getEmail());
                ps.setString(paramIndex++, business.getLogoUrl());
                ps.setDate(paramIndex, new java.sql.Date(business.getEstablishmentDate().getTime()));
                return ps;
            }, keyHolder);

            Long generatedId = keyHolder.getKey().longValue();
            business.setId(generatedId);
        } else {
            String sql = "UPDATE learna_business SET name = ?, description = ?, address = ?, phone = ?, email = ?, logo_url = ?, establishment_date = ?, modified_dte = NOW() WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql,
                    business.getName(),
                    business.getDescription(),
                    business.getAddress(),
                    business.getPhone(),
                    business.getEmail(),
                    business.getLogoUrl(),
                    new java.sql.Date(business.getEstablishmentDate().getTime()),
                    business.getId());

            if (rowsAffected == 0) {
                throw new RuntimeException("Business not found with id: " + business.getId());
            }
        }

        return business;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM learna_business WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        if (rowsAffected == 0) {
            throw new RuntimeException("Business not found with id: " + id);
        }
    }
}