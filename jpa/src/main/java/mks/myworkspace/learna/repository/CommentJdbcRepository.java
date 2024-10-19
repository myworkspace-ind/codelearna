package mks.myworkspace.learna.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.entity.User;

@Repository
public class CommentJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Comment> findByLessonIdAndParentCommentIsNullOrderByCreatedDateDesc(Long lessonId) {
        String sql = "SELECT c.*, u.* FROM learna_comment c " +
                     "JOIN learna_user u ON c.user_id = u.id " +
                     "WHERE c.lesson_id = ? AND c.parent_comment_id IS NULL " +
                     "ORDER BY c.created_dte DESC";
        
        return jdbcTemplate.query(sql, new CommentRowMapper(), lessonId);
    }

    public Comment findById(Long id) {
        String sql = "SELECT c.*, u.* FROM learna_comment c " +
                     "JOIN learna_user u ON c.user_id = u.id " +
                     "WHERE c.id = ?";
        
        return jdbcTemplate.queryForObject(sql, new CommentRowMapper(), id);
    }

    public void save(Comment comment) {
        if (comment.getId() == null) {
            // Insert
            String sql = "INSERT INTO learna_comment (content, lesson_id, user_id, parent_comment_id, created_dte, modified_dte) " +
                         "VALUES (?, ?, ?, ?, NOW(), NOW())";
            jdbcTemplate.update(sql, comment.getContent(), comment.getLesson().getId(), 
                                comment.getUser().getId(), comment.getParentComment() != null ? comment.getParentComment().getId() : null);
        } else {
            // Update
            String sql = "UPDATE learna_comment SET content = ?, modified_dte = NOW() WHERE id = ?";
            jdbcTemplate.update(sql, comment.getContent(), comment.getId());
        }
    }

    public List<Comment> findChildComments(Long parentId) {
        String sql = "SELECT c.*, u.* FROM learna_comment c " +
                     "JOIN learna_user u ON c.user_id = u.id " +
                     "WHERE c.parent_comment_id = ? " +
                     "ORDER BY c.created_dte ASC";
        
        return jdbcTemplate.query(sql, new CommentRowMapper(), parentId);
    }

    private class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Comment comment = new Comment();
            comment.setId(rs.getLong("id"));
            comment.setContent(rs.getString("content"));
            
            Lesson lesson = new Lesson();
            lesson.setId(rs.getLong("lesson_id"));
            comment.setLesson(lesson);
            
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setUsername(rs.getString("username"));
            comment.setUser(user);
            
            comment.setCreatedDate(rs.getTimestamp("created_dte"));
            comment.setModifiedDate(rs.getTimestamp("modified_dte"));
            
            return comment;
        }
    }
}