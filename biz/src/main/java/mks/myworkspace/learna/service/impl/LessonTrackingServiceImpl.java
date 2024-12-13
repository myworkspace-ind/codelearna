package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.LessonTracking;
import mks.myworkspace.learna.repository.LessonRepository;
import mks.myworkspace.learna.repository.LessonTrackingRepository;
import mks.myworkspace.learna.service.LessonTrackingService;
import mks.myworkspace.learna.service.UserLibraryCourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LessonTrackingServiceImpl implements LessonTrackingService {
	private static final Logger logger = LoggerFactory.getLogger(LessonTrackingServiceImpl.class);
    private final LessonTrackingRepository lessonTrackingRepository;
    @Autowired
    private LessonRepository lessonRepository;
    
    @Autowired
    private UserLibraryCourseService userLibraryCourseService;
    @Autowired
    public LessonTrackingServiceImpl(LessonTrackingRepository lessonTrackingRepository) {
        this.lessonTrackingRepository = lessonTrackingRepository;
    }

    @Override
    public void saveTracking(String userEid, Long courseId, Long lessonId, String courseUrl, String activityId) {
        // Kiểm tra xem thông tin đã tồn tại trong bảng chưa
        boolean exists = lessonTrackingRepository.findByUserEidAndCourseIdAndLessonId(userEid, courseId, lessonId).isPresent();

        if (!exists) {
            // Tạo bản ghi mới nếu chưa tồn tại
            LessonTracking tracking = new LessonTracking();
            tracking.setUserEid(userEid);
            tracking.setCourseId(courseId);
            tracking.setLessonId(lessonId);
            tracking.setCourseUrl(courseUrl);
            tracking.setActivityId(activityId);
            lessonTrackingRepository.save(tracking);
            
            long completedLessons = lessonTrackingRepository.countCompletedLessonsByUserAndCourse(userEid, courseId);
            long totalLessons = lessonRepository.countLessonsByCourseIdAndStatusActive(courseId);
            logger.info("UserEid: {}, CourseId: {}, CompletedLessons: {}, TotalLessons: {}",
                    userEid, courseId, completedLessons, totalLessons);
            
            if (completedLessons == totalLessons) {
            	logger.info("dã thỏa");
                userLibraryCourseService.updateCourseProgressStatus(userEid, courseId);
            }
        }
    }
}
