package mks.myworkspace.learna.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Lesson;

@Repository
public class LessonJdbcRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public Lesson save(Lesson lesson) {
        if (lesson.getId() == null) {
            // Insert mới
            String sql = "INSERT INTO learna_lesson (title, video_url, course_id, created_dte, modified_dte) "
                    + "VALUES (?, ?, ?, NOW(), NOW())";
            
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int paramIndex = 1;
                ps.setString(paramIndex++, lesson.getTitle());
                ps.setString(paramIndex++, lesson.getVideoUrl());
                ps.setLong(paramIndex, lesson.getCourse().getId());
                return ps;
            }, keyHolder);
            
            Long generatedId = keyHolder.getKey().longValue();
            lesson.setId(generatedId);
            
        } else {
            // Update 
            String sql = "UPDATE learna_lesson SET title = ?, video_url = ?, "
                    + "course_id = ?, modified_dte = NOW() WHERE id = ?";
                    
            int rowsAffected = jdbcTemplate.update(sql,
                lesson.getTitle(),
                lesson.getVideoUrl(), 
                lesson.getCourse().getId(),
                lesson.getId()
            );
            
            if (rowsAffected == 0) {
                throw new RuntimeException("Lesson not found with id: " + lesson.getId());
            }
        }
        return lesson;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM learna_lesson WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        
        if (rowsAffected == 0) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
    }
}