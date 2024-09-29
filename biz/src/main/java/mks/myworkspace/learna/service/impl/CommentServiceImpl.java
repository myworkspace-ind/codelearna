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
            List<Comment> comments = commentRepository.findByLessonIdAndParentCommentIsNullOrderByCreatedDateDesc(lessonId);
            comments.forEach(this::initializeChildComments);
            return comments;
        } catch (Exception e) {
            throw e;
        }
    }

  
    private void initializeChildComments(Comment comment) {
        if (comment.getChildComments() != null) {
            Hibernate.initialize(comment.getChildComments());
            comment.getChildComments().forEach(this::initializeChildComments);
        }
       
        Hibernate.initialize(comment.getUser());
    }
    
    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    
    @Override
    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            return optionalComment.get();
        } else {
            throw new RuntimeException("Comment not found with id: " + id);
        }
    }
}
