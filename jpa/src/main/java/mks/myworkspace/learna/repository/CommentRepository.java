package mks.myworkspace.learna.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import mks.myworkspace.learna.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByLessonId(Long lessonId, Pageable pageable);
    
    Page<Comment> findByLessonIdAndParentCommentIsNull(Long lessonId, Pageable pageable);
}
