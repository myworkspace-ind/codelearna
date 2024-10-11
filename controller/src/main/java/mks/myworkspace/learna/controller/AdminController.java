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
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

}
