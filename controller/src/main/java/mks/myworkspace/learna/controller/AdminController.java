package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Course.DifficultyLevel;
import mks.myworkspace.learna.entity.Course.LessonType;
import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.entity.Subcategory;
import mks.myworkspace.learna.service.CategoryService;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.LessonService;
import mks.myworkspace.learna.service.PlayService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	@Autowired
    private PlayService playService;
    @Autowired
    private LessonService lessonService;

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
	
	@DeleteMapping("/courses/delete/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> deleteCourse(@PathVariable("id") Long id) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        Course course = courseService.getCourseById(id);
	        if (course != null) {
	            courseService.deleteCourse(id);
	            response.put("status", "success");
	            response.put("message", "Course has been deleted successfully.");
	            return ResponseEntity.ok(response);
	        } else {
	            response.put("status", "error");
	            response.put("message", "Course not found.");
	            return ResponseEntity.badRequest().body(response);
	        }
	    } catch (Exception e) {
	        response.put("status", "error");
	        response.put("message", "An error occurred while trying to delete the course: " + e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	}

	 @GetMapping("/courses/edit/{id}")
	    public ModelAndView showEditCourseForm(@PathVariable("id") Long id) {
	        Course course = courseService.getCourseById(id);
	        if (course == null) {
	            return new ModelAndView("redirect:/admin/listCourse");
	        }

	        ModelAndView mav = new ModelAndView("fragments/adminEditCourse :: editCourse");
	        mav.addObject("course", course);
	        mav.addObject("categories", categoryService.getAllCategories());
	        return mav;
	    }


	    // Handle the submission of the edit course form
	    @PostMapping("/courses/edit/{id}")
	    public ModelAndView editCourse(@PathVariable("id") Long id, @ModelAttribute("course") Course course) {
	        Course existingCourse = courseService.getCourseById(id);
	        if (existingCourse == null) {
	            return new ModelAndView("redirect:/admin/listCourse");
	        }

	        // Update the course details
	        existingCourse.setName(course.getName());
	        existingCourse.setOriginalPrice(course.getOriginalPrice());
	        existingCourse.setDiscountedPrice(course.getDiscountedPrice());
	        existingCourse.setImageUrl(course.getImageUrl());
	        existingCourse.setDescription(course.getDescription());
	        existingCourse.setDifficultyLevel(course.getDifficultyLevel());

	        // Save updated course
	        courseService.saveCourse(existingCourse);

	        return new ModelAndView("redirect:/admin/listCourse");
	    }

	    @GetMapping("/courses/{id}/lessons")
	    public ModelAndView showLessonsByCourse(@PathVariable("id") Long courseId) {
	    	 Course course = courseService.getCourseById(courseId);
	        List<Lesson> lessons = playService.getLessonsByCourseId(courseId); // Lấy danh sách bài học từ PlayService

	        if (lessons == null || lessons.isEmpty()) {
	            return new ModelAndView("redirect:/admin/list-course");
	        }

	        ModelAndView mav = new ModelAndView("fragments/adminCourseLessons :: lessonsContent");
	        mav.addObject("course", course);
	        mav.addObject("lessons", lessons); // Thêm danh sách bài học vào Model
	        return mav;
	    }
	    
	    @GetMapping("/courses/{id}/lessons/add")
	    public ModelAndView showAddLessonForm(@PathVariable("id") Long courseId) {
	        ModelAndView mav = new ModelAndView("fragments/adminAddLesson :: addLessonForm");

	        // Lấy khóa học từ courseId
	        Course course = courseService.getCourseById(courseId);
	        if (course == null) {
	            // Nếu khóa học không tồn tại, chuyển hướng về trang danh sách khóa học
	            return new ModelAndView("redirect:/admin/listCourse");
	        }

	        // Tạo đối tượng Lesson mới và gán khóa học vào
	        Lesson lesson = new Lesson();
	        lesson.setCourse(course);

	        // Truyền đối tượng lesson và course vào ModelAndView để Thymeleaf sử dụng
	        mav.addObject("lesson", lesson);
	        mav.addObject("course", course); // Đảm bảo course được truyền vào Thymeleaf

	        return mav;
	    }

	    
	    @PostMapping("/courses/{courseId}/lessons/add")
	    public String addLesson(@PathVariable("courseId") Long courseId,
	                            @ModelAttribute("lesson") Lesson lesson,
	                            BindingResult bindingResult,
	                            RedirectAttributes redirectAttributes) {

	        // Kiểm tra nếu có lỗi
	        if (bindingResult.hasErrors()) {
	            redirectAttributes.addFlashAttribute("status", "error");
	            redirectAttributes.addFlashAttribute("message", "Thông tin bài học không hợp lệ.");
	            return "redirect:/admin/courses/" + courseId + "/lessons/add";
	        }

	        // Kiểm tra xem khóa học có tồn tại không
	        Course course = courseService.getCourseById(courseId);
	        if (course == null) {
	            redirectAttributes.addFlashAttribute("status", "error");
	            redirectAttributes.addFlashAttribute("message", "Khóa học không tồn tại.");
	            return "redirect:/admin/courses";
	        }

	        // Kiểm tra các trường thông tin của bài học
	        if (lesson.getTitle() == null || lesson.getTitle().isEmpty()) {
	            redirectAttributes.addFlashAttribute("status", "error");
	            redirectAttributes.addFlashAttribute("message", "Vui lòng điền tiêu đề bài học.");
	            return "redirect:/admin/courses/" + courseId + "/lessons/add";
	        }

	        // Gán bài học vào khóa học
	        lesson.setCourse(course);

	        try {
	            // Lưu bài học vào cơ sở dữ liệu
	            lessonService.saveLesson(lesson);
	            redirectAttributes.addFlashAttribute("status", "success");
	            redirectAttributes.addFlashAttribute("message", "Bài học đã được thêm thành công!");

	            // Điều hướng về trang danh sách bài học
	            return "redirect:/admin/courses/" + courseId + "/lessons";
	        } catch (Exception e) {
	            // Xử lý lỗi khi lưu bài học
	            redirectAttributes.addFlashAttribute("status", "error");
	            redirectAttributes.addFlashAttribute("message", "Có lỗi khi lưu bài học: " + e.getMessage());
	            return "redirect:/admin/courses/" + courseId + "/lessons/add";
	        }
	    }




	    @GetMapping("/lessons/edit/{id}")
	    public ModelAndView showEditLessonForm(@PathVariable("id") Long lessonId) {
	        Lesson lesson = playService.getLessonById(lessonId);
	        if (lesson == null) {
	            return new ModelAndView("redirect:/admin/listCourse");
	        }

	        ModelAndView mav = new ModelAndView("fragments/adminEditLesson :: editLessonForm");
	        mav.addObject("lesson", lesson);
	        return mav;
	    }

	    @PostMapping("/lessons/edit/{id}")
	    public ModelAndView editLesson(@PathVariable("id") Long lessonId, @ModelAttribute("lesson") Lesson lesson, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
	        if (bindingResult.hasErrors()) {
	            redirectAttributes.addFlashAttribute("status", "error");
	            redirectAttributes.addFlashAttribute("message", "Form contains errors. Please check again.");
	            return new ModelAndView("redirect:/admin/lessons/edit/" + lessonId);
	        }

	        // Retrieve the existing lesson
	        Lesson existingLesson = playService.getLessonById(lessonId);
	        if (existingLesson == null) {
	            redirectAttributes.addFlashAttribute("status", "error");
	            redirectAttributes.addFlashAttribute("message", "Lesson not found.");
	            return new ModelAndView("redirect:/admin/listCourse");
	        }

	        // Update lesson details
	        existingLesson.setTitle(lesson.getTitle());
	        existingLesson.setVideoUrl(lesson.getVideoUrl());

	        // Save the updated lesson
	        lessonService.saveLesson(existingLesson);
	        redirectAttributes.addFlashAttribute("status", "success");
	        redirectAttributes.addFlashAttribute("message", "Lesson updated successfully!");

	        return new ModelAndView("redirect:/admin/courses/" + existingLesson.getCourse().getId() + "/lessons");
	    }


	    // Xóa bài học
	    @DeleteMapping("/lessons/delete/{id}")
	    public ResponseEntity<Void> deleteLesson(@PathVariable("id") Long lessonId) {
	        lessonService.deleteLessonById(lessonId); 
	        return ResponseEntity.ok().build();
	    }

}