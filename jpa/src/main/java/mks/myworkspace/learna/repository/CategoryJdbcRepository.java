package mks.myworkspace.learna.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Category;

@Repository
public class CategoryJdbcRepository {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

	public Category save(Category category) {
	    if (category.getId() == null) {
	        String sql = "INSERT INTO learna_category (parameter_id) VALUES (?)";
	        KeyHolder keyHolder = new GeneratedKeyHolder();

	        jdbcTemplate.update(connection -> {
	            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            ps.setLong(1, category.getParameter().getId());
	            return ps;
	        }, keyHolder);

	        Long generatedId = keyHolder.getKey().longValue();
	        category.setId(generatedId);

	    } else {
	        String sql = "UPDATE learna_category SET parameter_id = ? WHERE id = ?";

	        int rowsAffected = jdbcTemplate.update(sql,
	                category.getParameter().getId(),
	                category.getId()
	        );

	        if (rowsAffected == 0) {
	            throw new RuntimeException("Category not found with id: " + category.getId());
	        }
	    }

	    return category;
	}

}
