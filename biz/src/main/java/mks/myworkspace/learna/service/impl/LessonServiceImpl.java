package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.repository.LessonRepository;
import mks.myworkspace.learna.repository.LessonJdbcRepository;
import mks.myworkspace.learna.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonJdbcRepository lessonJdbcRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, LessonJdbcRepository lessonJdbcRepository) {
        this.lessonRepository = lessonRepository;
        this.lessonJdbcRepository = lessonJdbcRepository;
    }

    @Override
    public void deleteLessonById(Long lessonId) {
        lessonJdbcRepository.deleteById(lessonId);
    }

    @Override
    public Lesson saveLesson(Lesson lesson) {
        return lessonJdbcRepository.save(lesson);
    }
}