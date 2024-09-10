package mks.myworkspace.learna.service;

import java.util.List;
import mks.myworkspace.learna.entity.Lesson;

public interface PlayService {
    List<Lesson> getLessonsByCourseId(Long courseId);
}
