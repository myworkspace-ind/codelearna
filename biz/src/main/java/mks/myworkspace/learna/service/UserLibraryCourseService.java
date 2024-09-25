package mks.myworkspace.learna.service;

import mks.myworkspace.learna.entity.UserLibraryCourse;
import java.util.List;

public interface UserLibraryCourseService {
    UserLibraryCourse saveUserLibraryCourse(UserLibraryCourse userLibraryCourse);
    UserLibraryCourse getUserLibraryCourseById(Long id);
    void deleteUserLibraryCourse(Long id);
    List<UserLibraryCourse> getUserLibraryCoursesByUserId(Long userId);
}