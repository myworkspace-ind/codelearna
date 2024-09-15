package mks.myworkspace.learna.service;

<<<<<<< HEAD
import java.util.List;
import mks.myworkspace.learna.entity.Lesson;

public interface PlayService {
    List<Lesson> getLessonsByCourseId(Long courseId);
=======
import mks.myworkspace.learna.entity.Lesson;
import java.util.List;

public interface PlayService {

    // Lấy danh sách bài học theo courseId
    List<Lesson> getLessonsByCourseId(Long courseId);

    // Lấy bài học theo lessonId
    Lesson getLessonById(Long lessonId);
>>>>>>> f75007b29de5c8d9f1f51ce9bfb9efbd0e3721d1
}
