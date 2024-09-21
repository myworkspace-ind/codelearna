package mks.myworkspace.learna.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.entity.User;
import mks.myworkspace.learna.repository.CommentRepository;
import mks.myworkspace.learna.repository.LessonRepository;
import mks.myworkspace.learna.repository.UserRepository;
import mks.myworkspace.learna.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByLessonIdAndParentCommentIsNull(Long lessonId) {
        try {
            List<Comment> comments = commentRepository.findByLessonIdAndParentCommentIsNull(lessonId);
            comments.forEach(this::initializeChildComments);
            logger.info("Loaded comments: {}", comments.size());
            return comments;
        } catch (Exception e) {
            logger.error("Error loading comments for lessonId {}: {}", lessonId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Comment createComment(Long lessonId, Long userId, String content) {
        Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (lessonOpt.isEmpty() || userOpt.isEmpty()) {
            return null; 
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setLesson(lessonOpt.get());
        comment.setUser(userOpt.get());
        comment.setCreatedDate(new Date());

        return commentRepository.save(comment);
    }

    private void initializeChildComments(Comment comment) {
        if (comment.getChildComments() != null) {
            Hibernate.initialize(comment.getChildComments());
            comment.getChildComments().forEach(this::initializeChildComments);
        }
        // Khởi tạo trước thông tin user để tránh lỗi LazyInitializationException
        Hibernate.initialize(comment.getUser());
    }

}
