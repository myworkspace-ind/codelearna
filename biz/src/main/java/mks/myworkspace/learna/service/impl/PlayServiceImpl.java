package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.repository.LessonRepository;
import mks.myworkspace.learna.service.PlayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PlayServiceImpl implements PlayService {

    private final LessonRepository lessonRepository;

    @Autowired
    public PlayServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<Lesson> getLessonsByCourseId(Long courseId) {
        return lessonRepository.findByCourseId(courseId);
    }

    @Override
    public Lesson getLessonById(Long lessonId) {
        Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
        return lessonOpt.orElse(null); // Trả về null nếu không tìm thấy bài học
    }
}
