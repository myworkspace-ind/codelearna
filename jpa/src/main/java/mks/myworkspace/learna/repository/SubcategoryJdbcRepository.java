package mks.myworkspace.learna.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Subcategory;

@Repository
public class SubcategoryJdbcRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public Subcategory save(Subcategory subcategory) {
	    if (subcategory.getId() == null) {
	        String sql = "INSERT INTO learna_subcategory (parameter_id, category_id) VALUES (?, ?)";

	        KeyHolder keyHolder = new GeneratedKeyHolder();

	        jdbcTemplate.update(connection -> {
	            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            int paramIndex = 1;
	            ps.setLong(paramIndex++, subcategory.getParameter().getId()); 
	            if (subcategory.getCategory() != null) {
	                ps.setLong(paramIndex++, subcategory.getCategory().getId()); 
	            } else {
	            	throw new RuntimeException("Missing parameter_id or category_id");
	            }
	            return ps;
	        }, keyHolder);

	        Long generatedId = keyHolder.getKey().longValue();
	        subcategory.setId(generatedId);

	    } else {
	        String sql = "UPDATE learna_subcategory SET parameter_id = ?, category_id = ? WHERE id = ?";

	        int rowsAffected = jdbcTemplate.update(sql,
	                subcategory.getParameter().getId(), 
	                subcategory.getCategory() != null ? subcategory.getCategory().getId() : null, 
	                subcategory.getId()
	        );

	        if (rowsAffected == 0) {
	            throw new RuntimeException("Subcategory not found with id: " + subcategory.getId());
	        }
	    }

	    return subcategory;
	}
}
