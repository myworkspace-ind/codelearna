package mks.myworkspace.learna.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import mks.myworkspace.learna.entity.Comment;

public interface CommentService {
    // Lấy tất cả bình luận theo lessonId
    Page<Comment> getCommentsByLessonId(Long lessonId, Pageable pageable);

    // Lấy bình luận cha (những bình luận không có parentComment)
    Page<Comment> getCommentsByLessonIdAndParentCommentIsNull(Long lessonId, Pageable pageable);
}
