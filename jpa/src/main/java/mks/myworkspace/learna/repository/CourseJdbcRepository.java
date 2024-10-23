package mks.myworkspace.learna.repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Course;

@Repository
public class CourseJdbcRepository {

	 @Autowired
	    private JdbcTemplate jdbcTemplate;

	    public Course save(Course course) {
	        if (course.getId() == null) {
	            String sql = "INSERT INTO learna_course (name, original_price, discounted_price, image_url, description, "
	                    + "difficulty_level, lesson_type, subcategory_id, is_free, created_dte, modified_dte) "
	                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

	            KeyHolder keyHolder = new GeneratedKeyHolder();

	          
	            jdbcTemplate.update(connection -> {
	                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	                ps.setString(1, course.getName());
	                ps.setDouble(2, course.getOriginalPrice());
	                ps.setDouble(3, course.getDiscountedPrice());
	                ps.setString(4, course.getImageUrl());
	                ps.setString(5, course.getDescription());
	                ps.setString(6, course.getDifficultyLevel().name());
	                ps.setString(7, course.getLessonType().name());
	                ps.setLong(8, course.getSubcategory() != null ? course.getSubcategory().getId() : null); 
	                ps.setBoolean(9, course.getIsFree() != null ? course.getIsFree() : false);
	                return ps;
	            }, keyHolder);

	            Long generatedId = keyHolder.getKey().longValue();
	            course.setId(generatedId);

	        } else {
	        
	            String sql = "UPDATE learna_course SET name = ?, original_price = ?, discounted_price = ?, image_url = ?, "
	                    + "description = ?, difficulty_level = ?, lesson_type = ?, subcategory_id = ?, is_free = ?, "
	                    +  "modified_dte = NOW() WHERE id = ?";

	            jdbcTemplate.update(sql, course.getName(), course.getOriginalPrice(), course.getDiscountedPrice(),
	                    course.getImageUrl(), course.getDescription(), course.getDifficultyLevel().name(),
	                    course.getLessonType().name(), course.getSubcategory() != null ? course.getSubcategory().getId() : null,
	                    course.getIsFree(), course.getId());
	        }

	        return course;
    }
}