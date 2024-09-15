package mks.myworkspace.learna.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.repository.CommentRepository;
import mks.myworkspace.learna.service.CommentService;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByLessonId(Long lessonId) {
        return commentRepository.findByLessonId(lessonId);
    }
}
