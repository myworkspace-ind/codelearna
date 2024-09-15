package mks.myworkspace.learna.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import mks.myworkspace.learna.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseId(Long courseId);
}
