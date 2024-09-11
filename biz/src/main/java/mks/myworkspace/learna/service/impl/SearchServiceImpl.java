package mks.myworkspace.learna.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.repository.SearchRepository;
import mks.myworkspace.learna.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchRepository searchRepository;

	@Override
	public SearchRepository getRepo() {
		return searchRepository;
	}

	// Hiển thị tất cả các khoá học (search empty)
	@Override
	public List<Course> getAllCourses(){
		return searchRepository.findAll();
	}
	// Tìm kiếm khóa học theo từ khóa
	@Override
	public List<Course> searchCoursesByKeyword(String keyword) {
		return searchRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
	}

}
