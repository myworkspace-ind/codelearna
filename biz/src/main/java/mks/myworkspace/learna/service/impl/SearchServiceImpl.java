package mks.myworkspace.learna.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.SearchRepository;
import mks.myworkspace.learna.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchRepository searchRepository;


	@Override
	public List<Course> searchCoursesByKeywordAndSortFilter(String keyword, String sortOrder) {
		if ("asc".equals(sortOrder)) {
	        return searchRepository.findByNameContainingOrDescriptionContainingOrderByCreatedDateAsc(keyword, keyword);
	    } else if("desc".equals(sortOrder)){
	        return searchRepository.findByNameContainingOrDescriptionContainingOrderByCreatedDateDesc(keyword, keyword);
	    } else if("priceAsc".equals(sortOrder)) {
	    	return searchRepository.findByNameContainingOrDescriptionContainingOrderByDiscountedPriceAsc(keyword, sortOrder);
	    } else {
	    	return searchRepository.findByNameContainingOrDescriptionContainingOrderByDiscountedPriceDesc(keyword, sortOrder); 
	    }
	}


	@Override
	public List<Course> getAllCoursesSortedBySortFilter(String sortOrder) {
		if ("asc".equals(sortOrder)) {
			return searchRepository.findAllByOrderByCreatedDateAsc();
		} else if("desc".equals(sortOrder)){
			return searchRepository.findAllByOrderByCreatedDateDesc();
		} else if("priceAsc".equals(sortOrder)) {
			return searchRepository.findAllByOrderByDiscountedPriceAsc();
		} else {
			return searchRepository.findAllByOrderByDiscountedPriceDesc();
		}
	}

}
