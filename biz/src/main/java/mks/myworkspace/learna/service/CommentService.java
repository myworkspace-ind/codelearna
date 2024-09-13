package mks.myworkspace.learna.service;

import java.util.List;
import mks.myworkspace.learna.entity.Comment;

public interface CommentService {
  
    List<Comment> getCommentsByLessonId(Long lessonId);
}
