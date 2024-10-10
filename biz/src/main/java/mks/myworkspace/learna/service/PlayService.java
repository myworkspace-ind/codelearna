package mks.myworkspace.learna.service;

import mks.myworkspace.learna.entity.Lesson;
import java.util.List;

public interface PlayService {

    // Lấy danh sách bài học theo courseId
    List<Lesson> getLessonsByCourseId(Long courseId);

    // Lấy bài học theo lessonId
    Lesson getLessonById(Long lessonId);
}
