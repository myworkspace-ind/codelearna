package mks.myworkspace.learna.service.impl;
<<<<<<< HEAD
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
=======
>>>>>>> f75007b29de5c8d9f1f51ce9bfb9efbd0e3721d1

import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.repository.LessonRepository;
import mks.myworkspace.learna.service.PlayService;
<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
>>>>>>> f75007b29de5c8d9f1f51ce9bfb9efbd0e3721d1

@Service
public class PlayServiceImpl implements PlayService {

<<<<<<< HEAD
    @Autowired
    private LessonRepository lessonRepository;
=======
    private final LessonRepository lessonRepository;

    @Autowired
    public PlayServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }
>>>>>>> f75007b29de5c8d9f1f51ce9bfb9efbd0e3721d1

    @Override
    public List<Lesson> getLessonsByCourseId(Long courseId) {
        return lessonRepository.findByCourseId(courseId);
    }
<<<<<<< HEAD
=======

    @Override
    public Lesson getLessonById(Long lessonId) {
        Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
        return lessonOpt.orElse(null); // Trả về null nếu không tìm thấy bài học
    }
>>>>>>> f75007b29de5c8d9f1f51ce9bfb9efbd0e3721d1
}
