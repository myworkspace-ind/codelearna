package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Course.DifficultyLevel;
import mks.myworkspace.learna.entity.Course.LessonType;
import mks.myworkspace.learna.entity.Subcategory;
import mks.myworkspace.learna.service.CategoryService;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.SubcategoryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SubcategoryService subCategoryService;

	// Hiển thị trang admin home
	@GetMapping
	public String showAdminHomePage() {
		return "adminHome";
	}

	@GetMapping("/listCourse")
	public ModelAndView loadCoursesFragment() {
		ModelAndView mav = new ModelAndView("fragments/adminListCourse :: coursesContent");
		mav.addObject("courses", courseService.getAllCourses());
		return mav;
	}

	@GetMapping("/addCourse")
	public ModelAndView showAddCoursePage() {
		ModelAndView mav = new ModelAndView("fragments/adminAddCourse");
		return mav;
	}

	@PostMapping("/addCourse")
	public String addCourse(
	        @RequestParam String name,
	        @RequestParam Double originalPrice,
	        @RequestParam Double discountedPrice,
//	        @RequestParam MultipartFile image, 
	        @RequestParam(required = false) Boolean isFree,
	        @RequestParam String description, 
	        @RequestParam String difficultyLevel, 
	        @RequestParam String lessonType, 
	        @RequestParam(required = false) Long subcategory,
	        Model model){

	    if (name.isEmpty() || originalPrice == null || discountedPrice == null || description.isEmpty()) {
	    	// Add noti
	    	
	        return "fragments/adminAddCourse"; 
	    }

//	    String base64Image = encodeImageToBase64(image);


	    Subcategory subcategoryObj = null;
	    if (subcategory != null) {
	        subcategoryObj = subCategoryService.getSubcategoryById(subcategory);
	    }


	    Course course = new Course();
	    course.setName(name);
	    course.setOriginalPrice(originalPrice);
	    course.setDiscountedPrice(discountedPrice);
//	    course.setImageUrl(base64Image);
	    course.setDescription(description);
	    course.setIsFree(isFree != null ? isFree : false); 
	    course.setSubcategory(subcategoryObj);
	    
	    try {
	        course.setDifficultyLevel(DifficultyLevel.valueOf(difficultyLevel)); 
	    } catch (IllegalArgumentException e) {
	        
	        return "fragments/adminAddCourse"; 
	    }


	    try {
	        course.setLessonType(LessonType.valueOf(lessonType));
	    } catch (IllegalArgumentException e) {
	        return "fragments/adminAddCourse"; 
	    }

	    courseService.saveCourse(course);

	    return "fragments/adminListCourse :: coursesContent";
	}

	public String encodeImageToBase64(MultipartFile file) throws IOException {
	    byte[] bytes = file.getBytes();
	    return Base64.getEncoder().encodeToString(bytes);
	}



}
