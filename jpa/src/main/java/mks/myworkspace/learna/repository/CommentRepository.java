package mks.myworkspace.learna.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import mks.myworkspace.learna.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByLessonIdAndParentCommentIsNullOrderByCreatedDateDesc(Long lessonId);
    
}
