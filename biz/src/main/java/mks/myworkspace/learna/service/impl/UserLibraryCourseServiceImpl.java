package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.User;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.UserLibraryCourse;
import mks.myworkspace.learna.repository.UserRepository;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.UserLibraryCourseRepository;
import mks.myworkspace.learna.service.UserLibraryCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLibraryCourseServiceImpl implements UserLibraryCourseService {

    private final UserLibraryCourseRepository userLibraryCourseRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public UserLibraryCourseServiceImpl(UserLibraryCourseRepository userLibraryCourseRepository,
                                        UserRepository userRepository,
                                        CourseRepository courseRepository) {
        this.userLibraryCourseRepository = userLibraryCourseRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public UserLibraryCourse saveUserLibraryCourse(UserLibraryCourse userLibraryCourse) {
        return userLibraryCourseRepository.save(userLibraryCourse);
    }

    @Override
    public UserLibraryCourse getUserLibraryCourseById(Long id) {
        return userLibraryCourseRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUserLibraryCourse(Long id) {
        userLibraryCourseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLibraryCourse> getUserLibraryCoursesByUserId(Long userId) {
        return userLibraryCourseRepository.findByUserId(userId);
    }

    @Override
    public void addCourseToLibrary(Long userId, Long courseId, UserLibraryCourse.PaymentStatus paymentStatus, UserLibraryCourse.ProgressStatus progressStatus) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        UserLibraryCourse userLibraryCourse = new UserLibraryCourse();
        userLibraryCourse.setUser(user);
        userLibraryCourse.setCourse(course);
        userLibraryCourse.setPaymentStatus(paymentStatus);
        userLibraryCourse.setProgressStatus(progressStatus);
        userLibraryCourseRepository.save(userLibraryCourse);
    }
}