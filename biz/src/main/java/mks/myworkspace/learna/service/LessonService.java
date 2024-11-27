package mks.myworkspace.learna.service;

import mks.myworkspace.learna.entity.Lesson;
import java.util.List;

public interface LessonService {
    
    void deleteLessonById(Long lessonId);
    Lesson saveLesson(Lesson lesson);
}
