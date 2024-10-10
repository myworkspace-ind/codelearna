package mks.myworkspace.learna.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mks.myworkspace.learna.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseId(Long courseId);

    @Query("SELECT l FROM Lesson l JOIN FETCH l.comments WHERE l.id = :lessonId")
    Optional<Lesson> findByIdWithComments(@Param("lessonId") Long lessonId);

}
