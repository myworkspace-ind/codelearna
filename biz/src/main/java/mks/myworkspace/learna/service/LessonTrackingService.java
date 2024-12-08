package mks.myworkspace.learna.service;

public interface LessonTrackingService {
    void saveTracking(String userEid, Long courseId, Long lessonId, String courseUrl, String activityId);
}
