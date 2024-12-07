package mks.myworkspace.learna.service;

import mks.myworkspace.learna.entity.UserLibraryCourse;
import java.util.List;

public interface UserLibraryCourseService {
    UserLibraryCourse saveUserLibraryCourse(UserLibraryCourse userLibraryCourse);
    UserLibraryCourse getUserLibraryCourseById(Long id);
    void deleteUserLibraryCourse(Long id);
    List<UserLibraryCourse> getUserLibraryCoursesByUserEid(String userEId);
    void addCourseToLibrary(String userId, Long courseId, UserLibraryCourse.PaymentStatus paymentStatus, UserLibraryCourse.ProgressStatus progressStatus);
    boolean isCoursePurchased(String userEid, Long courseId);
    boolean isCourseCompleted(String userEid, Long courseId);
    void updateCourseProgressStatus(String userEid, Long courseId);
    int calculateCompletionPercentage(String userEid, Long courseId);
}