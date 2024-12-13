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
            String sql = "INSERT INTO learna_lesson (title, video_url, course_id, activity_id, created_dte, modified_dte, status) "
                    + "VALUES (?, ?, ?, ?, NOW(), NOW(), ?)";  // Thêm status khi tạo mới bài học
            
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int paramIndex = 1;
                ps.setString(paramIndex++, lesson.getTitle());
                ps.setString(paramIndex++, lesson.getVideoUrl());
                ps.setLong(paramIndex++, lesson.getCourse().getId());
                ps.setString(paramIndex++, lesson.getActivityId());
                ps.setString(paramIndex++, lesson.getStatus() != null ? lesson.getStatus() : "ACTIVE");  // Default status = "ACTIVE"
                return ps;
            }, keyHolder);
            
            Long generatedId = keyHolder.getKey().longValue();
            lesson.setId(generatedId);
            
        } else {
            // Update 
            String sql = "UPDATE learna_lesson SET title = ?, video_url = ?, "
                    + "course_id = ?, activity_id = ?, modified_dte = NOW(), status = ? WHERE id = ?";
                    
            int rowsAffected = jdbcTemplate.update(sql,
                lesson.getTitle(),
                lesson.getVideoUrl(), 
                lesson.getCourse().getId(),
                lesson.getActivityId(),
                lesson.getStatus(),  // Cập nhật status
                lesson.getId()
            );
            
            if (rowsAffected == 0) {
                throw new RuntimeException("Lesson not found with id: " + lesson.getId());
            }
        }
        return lesson;
    }

   
    public void deleteById(Long id) {
        String sql = "UPDATE learna_lesson SET status = 'DELETED', modified_dte = NOW() WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        
        if (rowsAffected == 0) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
    }
}
