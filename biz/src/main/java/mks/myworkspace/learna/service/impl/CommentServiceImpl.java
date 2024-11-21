package mks.myworkspace.learna.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.repository.CommentJdbcRepository;
import mks.myworkspace.learna.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentJdbcRepository commentJdbcRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByLessonIdAndParentCommentIsNull(Long lessonId) {
        try {
            List<Comment> parentComments = commentJdbcRepository.findByLessonIdAndParentCommentIsNullOrderByCreatedDateDesc(lessonId);
            for (Comment parentComment : parentComments) {
                List<Comment> childComments = commentJdbcRepository.findChildComments(parentComment.getId());
                parentComment.setChildComments(childComments);
            }
            return parentComments;
        } catch (Exception e) {
            logger.error("Error fetching comments for lesson id: " + lessonId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void saveComment(Comment comment) {
        try {
            commentJdbcRepository.save(comment);
        } catch (Exception e) {
            logger.error("Error saving comment: " + comment, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        try {
            Comment comment = commentJdbcRepository.findById(id);
            if (comment != null) {
                List<Comment> childComments = commentJdbcRepository.findChildComments(comment.getId());
                comment.setChildComments(childComments);
            }
            return comment;
        } catch (Exception e) {
            logger.error("Error finding comment with id: " + id, e);
            throw new RuntimeException("Comment not found with id: " + id);
        }
    }
}