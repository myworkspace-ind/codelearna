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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
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
		mav.addObject("course", new Course());
		return mav;
	}

	@PostMapping("/addCourse")
	@ResponseBody
	public ResponseEntity<Map<String, String>> addCourse(@ModelAttribute("course") Course course,
			BindingResult bindingResult) {
		Map<String, String> response = new HashMap<>();

		if (course.getName() == null || course.getName().isEmpty() || course.getOriginalPrice() == null
				|| course.getDiscountedPrice() == null || course.getDescription() == null
				|| course.getDescription().isEmpty() || course.getSubcategory() == null) {

			response.put("status", "error");
			response.put("message", "Vui lòng điền đầy đủ thông tin.");
			return ResponseEntity.badRequest().body(response);
		}

		Subcategory subcategoryObj = null;
		if (course.getSubcategory() != null) {
			subcategoryObj = subCategoryService.getSubcategoryById(course.getSubcategory().getId());
			course.setSubcategory(subcategoryObj);
		}

		try {
			course.setLessonType(LessonType.valueOf(course.getLessonType().name()));
		} catch (IllegalArgumentException e) {
			response.put("status", "error");
			response.put("message", "Có lỗi khi lưu khóa học.");
			return ResponseEntity.badRequest().body(response);
		}

		try {
			courseService.saveCourse(course);
			response.put("status", "success");
			response.put("message", "Khóa học đã được thêm thành công!");

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "Có lỗi khi lưu khóa học.");
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/saveCoursesHandsontable")
	public ResponseEntity<Map<String, String>> saveCoursesHandsontable(@RequestBody List<Map<String, Object>> courseData) {
		log.info("Received request to save courses");
		Map<String, String> response = new HashMap<>();
		try {
			log.info("Received course data: {}", courseData);

			if (courseData == null || courseData.isEmpty()) {
				throw new IllegalArgumentException("Không có dữ liệu khóa học được gửi");
			}

			for (Map<String, Object> courseMap : courseData) {
				Course course = new Course();
				course.setName((String) courseMap.get("name"));
				course.setOriginalPrice((Double) courseMap.get("originalPrice"));
				course.setDiscountedPrice((Double) courseMap.get("discountedPrice"));
				course.setDescription((String) courseMap.get("description"));
				
				String difficultyLevelString = (String) courseMap.get("difficultyLevel");	
				if (difficultyLevelString == null || difficultyLevelString.isEmpty()) {
					difficultyLevelString = "BEGINNER"; 
				}
				course.setDifficultyLevel(DifficultyLevel.valueOf(difficultyLevelString.toUpperCase()));

				String lessonTypeString = (String) courseMap.get("lessonType");
	            if (lessonTypeString == null || lessonTypeString.isEmpty()) {
	            	lessonTypeString = "VIDEO"; 
	            }
	            course.setLessonType(LessonType.valueOf(lessonTypeString.toUpperCase()));
				
				if (course.getName() == null || course.getName().isEmpty()) {
					throw new IllegalArgumentException("Tên khóa học không được để trống");
				}
				if (course.getOriginalPrice() == null || course.getOriginalPrice() < 0) {
					throw new IllegalArgumentException("Giá gốc không hợp lệ");
				}

				courseService.saveCourse(course);
				log.info("Saved course: {}", course.getName());
			}

			response.put("status", "success");
			response.put("message", "Các khóa học đã được thêm thành công!");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error saving courses: ", e);
			response.put("status", "error");
			response.put("message", "Có lỗi khi lưu khóa học: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/addCourseHandsontable")
	public ModelAndView showAddCourseHandsontablePage() {
		ModelAndView mav = new ModelAndView("fragments/adminAddCoursesHandsontable");
		return mav;
	}

}
