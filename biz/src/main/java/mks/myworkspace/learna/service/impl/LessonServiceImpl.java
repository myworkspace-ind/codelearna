package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.repository.LessonRepository;
import mks.myworkspace.learna.service.LessonService;
import mks.myworkspace.learna.service.PlayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public void deleteLessonById(Long lessonId) {
        lessonRepository.deleteById(lessonId);  
    }
    @Override
    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);  
    }
}
