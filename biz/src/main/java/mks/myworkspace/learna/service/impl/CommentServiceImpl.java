package mks.myworkspace.learna.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.repository.CommentRepository;
import mks.myworkspace.learna.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByLessonId(Long lessonId) {
        return commentRepository.findByLessonId(lessonId);
    }
}
