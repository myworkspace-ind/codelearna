package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.UserLibraryCourse;
import mks.myworkspace.learna.repository.UserRepository;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.UserLibraryCourseRepository;
import mks.myworkspace.learna.repository.UserLibraryCourseJdbcRepository;
import mks.myworkspace.learna.service.UserLibraryCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLibraryCourseServiceImpl implements UserLibraryCourseService {

    private final UserLibraryCourseRepository userLibraryCourseRepository;
    private final UserLibraryCourseJdbcRepository userLibraryCourseJdbcRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public UserLibraryCourseServiceImpl(UserLibraryCourseRepository userLibraryCourseRepository,
                                        UserLibraryCourseJdbcRepository userLibraryCourseJdbcRepository,
                                        UserRepository userRepository,
                                        CourseRepository courseRepository) {
        this.userLibraryCourseRepository = userLibraryCourseRepository;
        this.userLibraryCourseJdbcRepository = userLibraryCourseJdbcRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public UserLibraryCourse saveUserLibraryCourse(UserLibraryCourse userLibraryCourse) {
        userLibraryCourseJdbcRepository.save(userLibraryCourse);
        return userLibraryCourse;
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
    public List<UserLibraryCourse> getUserLibraryCoursesByUserEid(String userEid) {
        return userLibraryCourseRepository.findByUserEid(userEid);
    }

    @Override
    public void addCourseToLibrary(String userEid, Long courseId, UserLibraryCourse.PaymentStatus paymentStatus, UserLibraryCourse.ProgressStatus progressStatus) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        // Kiểm tra xem UserLibraryCourse đã tồn tại chưa
        if (userLibraryCourseRepository.findByUserEidAndCourseId(userEid, courseId) != null) {
            throw new RuntimeException("Course already added to user's library");
        }

        UserLibraryCourse userLibraryCourse = new UserLibraryCourse();
        userLibraryCourse.setUserEid(userEid);
        userLibraryCourse.setCourse(course);
        userLibraryCourse.setPaymentStatus(paymentStatus);
        userLibraryCourse.setProgressStatus(progressStatus);
        userLibraryCourseJdbcRepository.save(userLibraryCourse);
    }
}
