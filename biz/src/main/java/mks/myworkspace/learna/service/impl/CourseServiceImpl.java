package mks.myworkspace.learna.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository repo;

    @Override
    public CourseRepository getRepo() {
        return repo;
    }

    @Override
    public Course saveCourse(Course course) {
        return repo.save(course);
    }

    @Override
    public Course getCourseById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteCourse(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return repo.findAll();
    }
    
 // Tìm kiếm khóa học theo từ khóa
    @Override
    public List<Course> searchCoursesByKeyword(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}
