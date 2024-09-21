package mks.myworkspace.learna.service;

import java.util.List;
import mks.myworkspace.learna.entity.Comment;

public interface CommentService {
    List<Comment> getCommentsByLessonIdAndParentCommentIsNull(Long lessonId);
    Comment createComment(Long lessonId, Long userId, String content);
}
