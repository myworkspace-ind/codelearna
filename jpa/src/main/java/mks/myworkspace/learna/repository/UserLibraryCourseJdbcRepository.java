package mks.myworkspace.learna.repository;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import mks.myworkspace.learna.entity.UserLibraryCourse;

@Repository
public class UserLibraryCourseJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(UserLibraryCourse userLibraryCourse) {
        if (userLibraryCourse.getId() == null) {
            // Insert
            String sql = "INSERT INTO learna_user_library_course (user_eid, course_id, payment_status, progress_status, created_dte, modified_dte) " +
                         "VALUES (?, ?, ?, ?, NOW(), NOW())";
            jdbcTemplate.update(sql, userLibraryCourse.getUserEid(), 
                                userLibraryCourse.getCourse().getId(), 
                                userLibraryCourse.getPaymentStatus().name(), 
                                userLibraryCourse.getProgressStatus().name());
        } else {
            // Update
            String sql = "UPDATE learna_user_library_course SET user_eid = ?, payment_status = ?, progress_status = ?, modified_dte = NOW() WHERE id = ?";
            jdbcTemplate.update(sql, userLibraryCourse.getUserEid(), 
                                userLibraryCourse.getPaymentStatus().name(), 
                                userLibraryCourse.getProgressStatus().name(), 
                                userLibraryCourse.getId());
        }
    }
}	