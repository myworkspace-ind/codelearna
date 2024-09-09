package mks.myworkspace.learna.service;

import java.util.List;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.repository.CourseRepository;

public interface CourseService {
    CourseRepository getRepo();
    Course saveCourse(Course course);
    Course getCourseById(Long id);
    void deleteCourse(Long id);
    List<Course> getAllCourses();
}
