package mks.myworkspace.learna.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.repository.CommentRepository;
import mks.myworkspace.learna.service.CommentService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByLessonId(Long lessonId, Pageable pageable) {
        return commentRepository.findByLessonId(lessonId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByLessonIdAndParentCommentIsNull(Long lessonId, Pageable pageable) {
        Page<Comment> commentsPage = commentRepository.findByLessonIdAndParentCommentIsNull(lessonId, pageable);
        
        // Explicitly initialize child comments to avoid LazyInitializationException
        commentsPage.forEach(comment -> comment.getChildComments().size());

        return commentsPage;
    }
}
