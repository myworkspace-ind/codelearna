package mks.myworkspace.learna.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.entity.User;

@Repository
public class ReviewJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public Review findReviewById(Long reviewId) {
        String sql = "SELECT r.id AS review_id, r.rating_star, r.content, r.course_id, r.user_id, " +
                     "u.id AS user_id, u.name, u.avatar_url, u.email " +
                     "FROM learna_review r " +
                     "JOIN learna_user u ON r.user_id = u.id " +
                     "WHERE r.id = ?";

        return jdbcTemplate.queryForObject(sql, new ReviewRowMapper(), reviewId);
    }


    public List<Review> findByCourseId(Long courseId) {
        String sql = "SELECT r.id AS review_id, r.rating_star, r.content, r.course_id, r.user_id, " +
                     "u.id AS user_id, u.name, u.avatar_url, u.email " +
                     "FROM learna_review r " +
                     "JOIN learna_user u ON r.user_id = u.id " +
                     "WHERE r.course_id = ?";

        return jdbcTemplate.query(sql, new ReviewRowMapper(), courseId);
    }

    public List<Double> findRatingsByCourseId(Long courseId) {
        String sql = "SELECT r.rating_star FROM learna_review r WHERE r.course_id = ?";
        return jdbcTemplate.queryForList(sql, Double.class, courseId);
    }

    public Double findAverageRatingByCourseId(Long courseId) {
        String sql = "SELECT AVG(r.rating_star) FROM learna_review r WHERE r.course_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, courseId);
    }

    public void save(Review review) {
        if (review.getId() == null) {
            // Insert mới
            String sql = "INSERT INTO learna_review (rating_star, content, course_id, user_id, user_eid, created_at, modified_at) " +
                         "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
            jdbcTemplate.update(sql, review.getRatingStar(), review.getContent(), 
                                review.getCourse().getId(), review.getUser().getId(), review.getUserEid());
        } else {
            // Update review
            String sql = "UPDATE learna_review SET rating_star = ?, content = ?, modified_at = NOW() WHERE id = ?";
            jdbcTemplate.update(sql, review.getRatingStar(), review.getContent(), review.getId());
        }
    }


    public void deleteById(Long reviewId) {
        String sql = "DELETE FROM learna_review WHERE id = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    private class ReviewRowMapper implements RowMapper<Review> {
        @Override
        public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
            Review review = new Review();
            review.setId(rs.getLong("review_id"));
            review.setRatingStar(rs.getInt("rating_star"));
            review.setContent(rs.getString("content"));
            
            Course course = new Course();
            course.setId(rs.getLong("course_id")); 
            review.setCourse(course);
            
            User user = new User();
            user.setId(rs.getLong("user_id")); 
            user.setName(rs.getString("name")); 
            user.setEmail(rs.getString("email"));
            user.setAvatarUrl(rs.getString("avatar_url"));
            review.setUser(user);

            
            return review;
        }
    }
}
