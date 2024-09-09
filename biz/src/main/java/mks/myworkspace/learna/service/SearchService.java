package mks.myworkspace.learna.service;

import java.util.List;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.repository.SearchRepository;


public interface SearchService {
    SearchRepository getRepo();
    List<Course> getAllCourses();
    List<Course> searchCoursesByKeyword(String keyword);
}
