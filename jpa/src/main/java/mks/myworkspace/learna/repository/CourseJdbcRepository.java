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

    @Autowired
    private ParameterRepository parameterRepository;

    public Course save(Course course) {
        if (course.getId() == null) {
        	String sql = "INSERT INTO learna_course (name, original_price, discounted_price, image_url, description, "
                    + "difficulty_level_id, lesson_type_id, subcategory_id, is_free, created_dte, modified_dte, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int paramIndex = 1;
                ps.setString(paramIndex++, course.getName());
                ps.setDouble(paramIndex++, course.getOriginalPrice());
                ps.setDouble(paramIndex++, course.getDiscountedPrice());
                ps.setString(paramIndex++, course.getImageUrl());
                ps.setString(paramIndex++, course.getDescription());
                ps.setLong(paramIndex++, course.getDifficultyLevel().getId());
                ps.setLong(paramIndex++, course.getLessonType().getId());
                ps.setLong(paramIndex++, course.getSubcategory().getId());
                ps.setBoolean(paramIndex++, course.getIsFree() != null ? course.getIsFree() : false);
                ps.setString(paramIndex++, course.getStatus() != null ? course.getStatus() : "ACTIVE"); 
                return ps;
            }, keyHolder);

            Long generatedId = keyHolder.getKey().longValue();
            course.setId(generatedId);
            

        } else {
        	 String sql = "UPDATE learna_course SET name = ?, original_price = ?, discounted_price = ?, "
                     + "image_url = ?, description = ?, difficulty_level_id = ?, lesson_type_id = ?, "
                     + "subcategory_id = ?, is_free = ?, modified_dte = NOW(), status = ? WHERE id = ?";

             int rowsAffected = jdbcTemplate.update(sql, 
                 course.getName(),
                 course.getOriginalPrice(),
                 course.getDiscountedPrice(),
                 course.getImageUrl(),
                 course.getDescription(),
                 course.getDifficultyLevel().getId(),
                 course.getLessonType().getId(),
                 course.getSubcategory().getId(),
                 course.getIsFree() != null ? course.getIsFree() : false,
                 course.getStatus(),
                 course.getId()
             );

             if (rowsAffected == 0) {
                 throw new RuntimeException("Course not found with id: " + course.getId());
             }
        }

        return course;
    }
    public void deleteById(Long id) {
        String sql = "UPDATE learna_course SET status = 'DELETED', modified_dte = NOW() WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        
        if (rowsAffected == 0) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
    }
}