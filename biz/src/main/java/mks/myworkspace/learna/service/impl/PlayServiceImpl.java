package mks.myworkspace.learna.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.repository.LessonRepository;
import mks.myworkspace.learna.service.PlayService;

@Service
public class PlayServiceImpl implements PlayService {

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public List<Lesson> getLessonsByCourseId(Long courseId) {
        return lessonRepository.findByCourseId(courseId);
    }
}
