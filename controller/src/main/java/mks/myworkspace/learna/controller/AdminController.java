package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Campaign;
import mks.myworkspace.learna.repository.CourseJdbcRepository;
import mks.myworkspace.learna.repository.ParameterRepository;
import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.entity.Parameter;
import mks.myworkspace.learna.entity.Subcategory;
import mks.myworkspace.learna.entity.UserLibraryCourse;
import mks.myworkspace.learna.service.CampaignService;
import mks.myworkspace.learna.service.CategoryService;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.LessonService;
import mks.myworkspace.learna.service.ParameterService;
import mks.myworkspace.learna.service.PlayService;
import mks.myworkspace.learna.service.RevenueService;
import mks.myworkspace.learna.service.SubcategoryService;
import mks.myworkspace.learna.service.UserLibraryCourseService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController extends BaseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private CampaignService campaignService;
	@Autowired
	private ParameterService parameterService;

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SubcategoryService subCategoryService;
	@Autowired
	private PlayService playService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private RevenueService revenueService;
	@Autowired
	private UserLibraryCourseService userLibraryCourseService;

	@GetMapping
	public String showAdminHomePage(Model model) {
		String currentUserEid = getCurrentUserEid();
		if (!"admin".equals(currentUserEid)) {
			return "accessDenied";
		}
		int totalCourses = courseService.getTotalCourses();
		Double totalRevenues = revenueService.getTotalRevenue();
		Long totalUsers = userLibraryCourseService.countTotalUserLibrary();
		model.addAttribute("totalCourses", totalCourses);
		model.addAttribute("totalUsers", totalUsers);
		model.addAttribute("totalRevenue", totalRevenues);
		return "adminHome";
	}

	@GetMapping("/dashboard")
	public String showDashBoard(Model model) {
		int totalCourses = courseService.getTotalCourses();
		Double totalRevenues = revenueService.getTotalRevenue();
		Long totalUsers = userLibraryCourseService.countTotalUserLibrary();
		model.addAttribute("totalCourses", totalCourses);
		model.addAttribute("totalUsers", totalUsers);
		model.addAttribute("totalRevenue", totalRevenues);
		return "fragments/dashboard :: dashboard";
	}

	@GetMapping("/listCourse")
	public ModelAndView loadCoursesFragment() {
		ModelAndView mav = new ModelAndView("fragments/adminListCourse :: coursesContent");
		mav.addObject("courses", courseService.getAllCourses());
		return mav;
	}
	@GetMapping("/listCampaign")
	public ModelAndView loadCampaignsFragment() {
	    ModelAndView mav = new ModelAndView("fragments/adminListCampaign :: campaignsContent");
	    mav.addObject("campaigns", campaignService.getAllCampaigns());
	    return mav;
	}
	@GetMapping("/revenue")
	public ModelAndView revenueFragment() {
		ModelAndView mav = new ModelAndView("fragments/revenue :: revenue");
		return mav;
	}
    @GetMapping("/addCampaign")
    public ModelAndView showAddCampaignPage() {
        ModelAndView mav = new ModelAndView("fragments/adminAddCampaign :: addCampaignContent");

        // Lấy danh sách các tham số liên quan đến campaign (Ví dụ: campaign types, campaign statuses)
        List<Parameter> campaignTypes = parameterService.getListParamsByParamValue("campaign_type");
        List<Parameter> campaignStatuses = parameterService.getListParamsByParamValue("campaign_status");

        // Truyền dữ liệu vào ModelAndView
        mav.addObject("campaignTypes", campaignTypes);
        mav.addObject("campaignStatuses", campaignStatuses);

        return mav;
    }

    @PostMapping("/addCampaign")
    @Transactional
    public ResponseEntity<Map<String, String>> addCampaign(@Validated @ModelAttribute("campaign") Campaign campaign,
                                                           BindingResult bindingResult) {
        Map<String, String> response = new HashMap<>();

        // Kiểm tra lỗi trong BindingResult
        if (bindingResult.hasErrors()) {
            response.put("status", "error");
            response.put("message", "Invalid information.");
            System.out.println(bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }

        // Kiểm tra các trường cần thiết
        if (campaign.getName() == null || campaign.getName().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Campaign name is required.");
            return ResponseEntity.badRequest().body(response);
        }

        if (campaign.getStartTime() == null) {
            response.put("status", "error");
            response.put("message", "Start time is required.");
            return ResponseEntity.badRequest().body(response);
        }

        if (campaign.getEndTime() == null) {
            response.put("status", "error");
            response.put("message", "End time is required.");
            return ResponseEntity.badRequest().body(response);
        }

        if (campaign.getShortDescription() == null || campaign.getShortDescription().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Short description is required.");
            return ResponseEntity.badRequest().body(response);
        }

        if (campaign.getCoverImageUrl() == null || campaign.getCoverImageUrl().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Cover image URL is required.");
            return ResponseEntity.badRequest().body(response);
        }

        // Lưu campaign
        try {
            campaignService.saveCampaign(campaign);
            response.put("status", "success");
            response.put("message", "The campaign has been added successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "System error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/addCampaignHandsontable")
    public ModelAndView showAddCampaignHandsontablePage() {
        ModelAndView mav = new ModelAndView("fragments/adminAddCampaignsHandsontable :: addCampaignsContent");
        return mav;
    }

	@GetMapping("/addCourse")
	public ModelAndView showAddCoursePage() {
		ModelAndView mav = new ModelAndView("fragments/adminAddCourse :: addCourseContent");
		List<Parameter> difficultyLevels = parameterService.getListParamsByParamValueAndStatus("difficulty_level",
				"ACTIVE");
		log.info("do kho" + difficultyLevels);
		List<Parameter> lessonTypes = parameterService.getListParamsByParamValueAndStatus("lesson_type", "ACTIVE");
		mav.addObject("difficultyLevels", difficultyLevels);
		mav.addObject("lessonTypes", lessonTypes);
		return mav;
	}

	@PostMapping("/addCourse")
	@Transactional
	public ResponseEntity<Map<String, String>> addCourse(@Validated @ModelAttribute("course") Course course,
			BindingResult bindingResult) {
		Map<String, String> response = new HashMap<>();

		if (bindingResult.hasErrors()) {
			response.put("status", "error");
			response.put("message", "Invalid information.");
			System.out.println(bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(response);
		}

		if (course.getName() == null || course.getName().isEmpty()) {
			response.put("status", "error");
			response.put("message", "Course name is required.");
			return ResponseEntity.badRequest().body(response);
		}
		if (course.getOriginalPrice() == null) {
			response.put("status", "error");
			response.put("message", "Original price is required.");
			return ResponseEntity.badRequest().body(response);
		}

		if (course.getDiscountedPrice() == null) {
			response.put("status", "error");
			response.put("message", "Discount price is required.");
			return ResponseEntity.badRequest().body(response);

		}

		if (course.getStatus() == null || course.getStatus().isEmpty()) {
			course.setStatus("INACTIVE");
		}

		if (course.getDifficultyLevel() == null || course.getDifficultyLevel().getId() == null) {
			response.put("status", "error");
			response.put("message", "Difficulty Level is required and must be valid.");
			return ResponseEntity.badRequest().body(response);
		}

		if (course.getLessonType() == null || course.getLessonType().getId() == null) {
			response.put("status", "error");
			response.put("message", "Lesson Type is required and must be valid.");
			return ResponseEntity.badRequest().body(response);
		}

		Parameter difficultyLevel = parameterService.getParameterById(course.getDifficultyLevel().getId());
		Parameter lessonType = parameterService.getParameterById(course.getLessonType().getId());

		if (difficultyLevel == null) {
			response.put("status", "error");
			response.put("message", "Difficulty Level is not valid.");
			return ResponseEntity.badRequest().body(response);
		}

		if (lessonType == null) {
			response.put("status", "error");
			response.put("message", "Lesson Type is not valid.");
			return ResponseEntity.badRequest().body(response);
		}

		course.setDifficultyLevel(difficultyLevel);
		course.setLessonType(lessonType);

		if (course.getSubcategory() == null || course.getSubcategory().getId() == null) {
			response.put("status", "error");
			response.put("message", "Subcategory is required and must be valid.");
			return ResponseEntity.badRequest().body(response);
		}

		try {
			courseService.saveCourse(course);
			response.put("status", "success");
			response.put("message", "The course has been added successfully!");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "System error: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/addCourseHandsontable")
	@ResponseBody
	public ModelAndView showAddCourseHandsontablePage() {
		ModelAndView mav = new ModelAndView("fragments/adminAddCoursesHandsontable :: addCoursesContent");
		return mav;
	}

	@ResponseBody
	@GetMapping("/values")
	public List<String> getParamValues(@RequestParam String paramKey) {
		return parameterService.getListParamsByParamValueAndStatus(paramKey, "ACTIVE").stream()
				.map(Parameter::getParamValue).collect(Collectors.toList());
	}



	@ResponseBody
	@GetMapping("/paramKey")
	public List<String> getParamKeys() {
		return parameterService.getAllDistinctParamKeys();
	}

	@PostMapping("/saveCoursesHandsontable")
	@Transactional
	public ResponseEntity<Map<String, String>> saveCoursesHandsontable(
			@RequestBody List<Map<String, Object>> courseData) {
		log.info("Received request to save courses");
		Map<String, String> response = new HashMap<>();

		try {
			log.info("Received course data: {}", courseData);

			if (courseData == null || courseData.isEmpty()) {
				throw new IllegalArgumentException("No course data was sent");
			}

			for (Map<String, Object> courseMap : courseData) {
				Course course = new Course();

				course.setName((String) courseMap.get("name"));

				// Xử lý chuyển đổi giá
				Object originalPriceObj = courseMap.get("originalPrice");
				if (originalPriceObj != null) {
					if (originalPriceObj instanceof Integer) {
						course.setOriginalPrice(((Integer) originalPriceObj).doubleValue());
					} else if (originalPriceObj instanceof String) {
						course.setOriginalPrice(Double.parseDouble((String) originalPriceObj));
					} else {
						course.setOriginalPrice((Double) originalPriceObj);
					}
				}

				Object discountedPriceObj = courseMap.get("discountedPrice");
				if (discountedPriceObj != null) {
					if (discountedPriceObj instanceof Integer) {
						course.setDiscountedPrice(((Integer) discountedPriceObj).doubleValue());
					} else if (discountedPriceObj instanceof String) {
						course.setDiscountedPrice(Double.parseDouble((String) discountedPriceObj));
					} else {
						course.setDiscountedPrice((Double) discountedPriceObj);
					}
				}

				course.setImageUrl((String) courseMap.get("imageUrl"));
				course.setDescription((String) courseMap.get("description"));

				if (course.getName() == null || course.getName().isEmpty()) {
					throw new IllegalArgumentException("Course name cannot be empty");
				}
				if (course.getOriginalPrice() == null || course.getOriginalPrice() < 0) {
					throw new IllegalArgumentException("Invalid original price");
				}
				if (course.getDiscountedPrice() == null || course.getDiscountedPrice() < 0) {
					throw new IllegalArgumentException("Invalid discounted price");
				}

				// Xử lý subcategory
				String subcategoryString = (String) courseMap.get("subcategory");
				log.info("Subcategory as string: {}", subcategoryString);
				Parameter subcategoryParameter = parameterService.getParameterByParamKeyAndParamValue("subcategory",
						subcategoryString);
				if (subcategoryParameter == null) {
					throw new IllegalArgumentException("Invalid subcategory: " + subcategoryString);
				}
				Subcategory subcategory = subCategoryService.getSubcategoryByParameter(subcategoryParameter);
				course.setSubcategory(subcategory);

				// Xử lý difficulty level
				String difficultyLevelString = (String) courseMap.get("difficultyLevel");
				Parameter difficultyLevel = parameterService.getParameterByParamKeyAndParamValue("difficulty_level",
						difficultyLevelString);
				if (difficultyLevel == null) {
					throw new IllegalArgumentException("Invalid difficulty level: " + difficultyLevelString);
				}
				course.setDifficultyLevel(difficultyLevel);

				// Xử lý lesson type
				String lessonTypeString = (String) courseMap.get("lessonType");
				Parameter lessonType = parameterService.getParameterByParamKeyAndParamValue("lesson_type",
						lessonTypeString);
				if (lessonType == null) {
					throw new IllegalArgumentException("Invalid lesson type: " + lessonTypeString);
				}
				course.setLessonType(lessonType);

				// Xử lý isFree
				Object isFreeObj = courseMap.get("isFree");
				course.setIsFree(isFreeObj instanceof Boolean ? (Boolean) isFreeObj : Boolean.FALSE);

				courseService.saveCourse(course);
			}

			response.put("status", "success");
			response.put("message", "The courses have been added successfully!");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error saving courses: ", e);
			response.put("status", "error");
			response.put("message", "An error occurred while saving the course: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/courses/delete/{id}")
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

	@PostMapping("/courses/toggleCourseStatus/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> toggleCourseStatus(@PathVariable("id") Long id,
			@ModelAttribute("course") Course course) {
		Map<String, String> response = new HashMap<>();
		try {
			Course existingCourse = courseService.getCourseById(id);
			if (existingCourse == null) {
				response.put("status", "error");
				response.put("message", "Course not found");
				return ResponseEntity.badRequest().body(response);
			}

			// Long courseId = existingLesson.getCourse().getId();
			if ("ACTIVE".equals(existingCourse.getStatus())) {
				existingCourse.setStatus("INACTIVE");
			} else {
				existingCourse.setStatus("ACTIVE");
			}

			courseService.saveCourse(existingCourse);
			response.put("status", "success");
			response.put("message", "Course has been toggle Course Status successfully");
			// response.put("courseId", courseId.toString());
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error deleting lesson: ", e);
			response.put("status", "error");
			response.put("message",
					"An error occurred while trying to toggleLessonStatus the course: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/courses/edit/{id}")
	public ModelAndView showEditCourseForm(@PathVariable("id") Long id) {
		Course course = courseService.getCourseById(id);
		if (course == null) {
			return new ModelAndView("redirect:/admin/listCourse");
		}

		ModelAndView mav = new ModelAndView("fragments/adminEditCourse :: editCourseModal");
		mav.addObject("course", course);
		mav.addObject("categories", categoryService.getAllCategories());
		mav.addObject("lessonTypes", parameterService.getListParamsByParamValue("lesson_type"));
		mav.addObject("difficultyLevel", parameterService.getListParamsByParamValue("difficulty_level"));
		return mav;
	}

	@PostMapping("/courses/edit/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> editCourse(@PathVariable("id") Long id,
			@ModelAttribute("course") Course course) {
		Map<String, String> response = new HashMap<>();

		try {
			Course existingCourse = courseService.getCourseById(id);
			if (existingCourse == null) {
				response.put("status", "error");
				response.put("message", "Course not found");
				return ResponseEntity.badRequest().body(response);
			}

			// Validate input
			if (course.getName() == null || course.getName().trim().isEmpty()) {
				response.put("status", "error");
				response.put("message", "Course name is required");
				return ResponseEntity.badRequest().body(response);
			}

			// Update existing course
			existingCourse.setName(course.getName());
			existingCourse.setOriginalPrice(course.getOriginalPrice());
			existingCourse.setDiscountedPrice(course.getDiscountedPrice());
			existingCourse.setImageUrl(course.getImageUrl());
			existingCourse.setDescription(course.getDescription());

			// Cập nhật difficultyLevel bằng cách lấy đối tượng Parameter theo ID
			if (course.getDifficultyLevel() != null && course.getDifficultyLevel().getId() != null) {
				Parameter difficultyLevel = parameterService.getParameterById(course.getDifficultyLevel().getId());
				existingCourse.setDifficultyLevel(difficultyLevel);
			}

			// Cập nhật lessonType bằng cách lấy đối tượng Parameter theo ID
			if (course.getLessonType() != null && course.getLessonType().getId() != null) {
				Parameter lessonType = parameterService.getParameterById(course.getLessonType().getId());
				existingCourse.setLessonType(lessonType);
			}

			existingCourse.setIsFree(course.getIsFree());

			if (course.getSubcategory() != null && course.getSubcategory().getId() != null) {
				existingCourse.setSubcategory(course.getSubcategory());
			}

			courseService.saveCourse(existingCourse);

			response.put("status", "success");
			response.put("message", "Course updated successfully");
			response.put("courseId", existingCourse.getId().toString());
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error updating course: ", e);
			response.put("status", "error");
			response.put("message", e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/listDeletedCourse")
	public ModelAndView showDeletedCourses() {
		List<Course> courses = courseService.getAllCourses();
		ModelAndView mav = new ModelAndView("fragments/adminListDeletedCourse :: deletedCourseModal");
		// Initialize with empty list if null
		mav.addObject("courses", courses != null ? courses : new ArrayList<>());
		return mav;
	}

	@PostMapping("/courses/restoreCourse/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> restoreCourse(@PathVariable("id") Long id,
			@ModelAttribute("course") Course course) {
		Map<String, String> response = new HashMap<>();
		try {
			Course existingCourse = courseService.getCourseById(id);
			if (existingCourse == null) {
				response.put("status", "error");
				response.put("message", "Course not found");
				return ResponseEntity.badRequest().body(response);
			}

			existingCourse.setStatus("INACTIVE");
			courseService.saveCourse(existingCourse);
			response.put("status", "success");
			response.put("message", "Course has been restore successfully");
			// response.put("courseId", courseId.toString());
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error deleting lesson: ", e);
			response.put("status", "error");
			response.put("message", "An error occurred while trying to restore the course: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/courses/{id}/lessons")
	public ModelAndView showLessonsByCourse(@PathVariable("id") Long courseId) {
		Course course = courseService.getCourseById(courseId);
		List<Lesson> lessons = playService.getLessonsByCourseId(courseId);

		/*
		 * if (lessons == null || lessons.isEmpty()) { return new
		 * ModelAndView("redirect:/admin/listCourse"); }
		 */

		ModelAndView mav = new ModelAndView("fragments/adminCourseLessons :: lessonsContent");
		mav.addObject("course", course);
		mav.addObject("lessons", lessons);
		return mav;
	}

	@GetMapping("/courses/{id}/deletedLessons")
	public ModelAndView showDeletedLessonOfCourse(@PathVariable("id") Long courseId) {
		Course course = courseService.getCourseById(courseId);
		List<Lesson> lessons = playService.getLessonsByCourseId(courseId);

		/*
		 * if (lessons == null || lessons.isEmpty()) { return new
		 * ModelAndView("redirect:/admin/listCourse"); }
		 */
		if (course == null) {

			return new ModelAndView("redirect:/admin/listCourse");
		}
		ModelAndView mav = new ModelAndView("fragments/adminListDeletedLesson :: deletedLessonModal");
		mav.addObject("course", course);
		mav.addObject("lessons", lessons);
		return mav;
	}

	@PostMapping("/lessons/restore/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> restoreLesson(@PathVariable("id") Long lessonId,
			@ModelAttribute("lesson") Lesson lesson) {
		Map<String, String> response = new HashMap<>();
		try {
			Lesson existingLesson = playService.getLessonById(lessonId);
			if (existingLesson == null) {
				response.put("status", "error");
				response.put("message", "Lesson not found");
				return ResponseEntity.badRequest().body(response);
			}

			Long courseId = existingLesson.getCourse().getId();

			existingLesson.setStatus("INACTIVE");

			lessonService.saveLesson(existingLesson);
			response.put("status", "success");
			response.put("message", "Lesson has been restore successfully");
			response.put("courseId", courseId.toString());
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error deleting lesson: ", e);
			response.put("status", "error");
			response.put("message", "An error occurred while trying to restore the lesson: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/courses/{id}/lessons/add")
	public ModelAndView showAddLessonForm(@PathVariable("id") Long courseId) {
		ModelAndView mav = new ModelAndView("fragments/adminAddLesson :: addLessonForm");
		Course course = courseService.getCourseById(courseId);
		if (course == null) {

			return new ModelAndView("redirect:/admin/listCourse");
		}

		Lesson lesson = new Lesson();
		lesson.setCourse(course);

		mav.addObject("lesson", lesson);
		mav.addObject("course", course);

		return mav;
	}

	@PostMapping("/courses/{courseId}/lessons/add")
	@ResponseBody
	public ResponseEntity<Map<String, String>> addLesson(@PathVariable("courseId") Long courseId,
			@ModelAttribute("lesson") Lesson lesson) {
		Map<String, String> response = new HashMap<>();

		try {
			// Validate course existence
			Course course = courseService.getCourseById(courseId);
			if (course == null) {
				response.put("status", "error");
				response.put("message", "Course not found");
				return ResponseEntity.badRequest().body(response);
			}

			// Validate lesson data
			if (lesson.getTitle() == null || lesson.getTitle().trim().isEmpty()) {
				response.put("status", "error");
				response.put("message", "Lesson title is required");
				return ResponseEntity.badRequest().body(response);
			}
			// Set default status to INACTIVE if not set
			if (lesson.getActivityId() == null || lesson.getStatus().isEmpty()) {
				response.put("status", "error");
				response.put("message", "Lesson activity id is required");
				return ResponseEntity.badRequest().body(response);
			}

			// Set default status to INACTIVE if not set
			if (lesson.getStatus() == null || lesson.getStatus().isEmpty()) {
				lesson.setStatus("INACTIVE");
			}

			lesson.setCourse(course);
			lessonService.saveLesson(lesson);

			response.put("status", "success");
			response.put("message", "Lesson added successfully");
			response.put("courseId", courseId.toString());
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error adding lesson: ", e);
			response.put("status", "error");
			response.put("message", "Error adding lesson: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/lessons/edit/{id}")
	public ModelAndView showEditLessonForm(@PathVariable("id") Long lessonId) {
		Lesson lesson = playService.getLessonById(lessonId);
		if (lesson == null) {
			return new ModelAndView("redirect:/admin/listCourse");
		}

		ModelAndView mav = new ModelAndView("fragments/adminEditLesson :: editLessonModal");
		mav.addObject("lesson", lesson);
		return mav;
	}

	@PostMapping("/lessons/edit/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> editLesson(@PathVariable("id") Long lessonId,
			@ModelAttribute("lesson") Lesson lesson) {
		Map<String, String> response = new HashMap<>();
		try {
			Lesson existingLesson = playService.getLessonById(lessonId);
			if (existingLesson == null) {
				response.put("status", "error");
				response.put("message", "Lesson not found");
				return ResponseEntity.badRequest().body(response);
			}

			existingLesson.setTitle(lesson.getTitle());
			existingLesson.setVideoUrl(lesson.getVideoUrl());
			existingLesson.setActivityId(lesson.getActivityId());

			lessonService.saveLesson(existingLesson);

			response.put("status", "success");
			response.put("message", "Lesson updated successfully");
			response.put("courseId", existingLesson.getCourse().getId().toString());
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "Error updating lesson: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/addLessonsHandsontable/{courseId}")
	public ModelAndView showAddLessonsHandsontablePage(@PathVariable("courseId") Long courseId) {

		Course course = courseService.getCourseById(courseId);
		if (course == null) {
			return new ModelAndView("redirect:/admin/listCourse");
		}

		ModelAndView mav = new ModelAndView("fragments/adminAddLessonsHandsontable :: addLessonsContent");
		mav.addObject("course", course);
		return mav;
	}

	@PostMapping("/saveLessonsHandsontable/{courseId}")
	public ResponseEntity<Map<String, String>> saveLessonsHandsontable(@PathVariable("courseId") Long courseId,
			@RequestBody List<Map<String, Object>> lessonData) {
		log.info("Received request to save lessons for courseId: {}", courseId);
		Map<String, String> response = new HashMap<>();

		try {
			log.info("Received lesson data: {}", lessonData);

			if (lessonData == null || lessonData.isEmpty()) {
				response.put("status", "error");
				response.put("message", "Please fill all the values");
				return ResponseEntity.badRequest().body(response);
			}

			Course course = courseService.getCourseById(courseId);
			if (course == null) {
				response.put("status", "error");
				response.put("message", "Course not found");
				return ResponseEntity.badRequest().body(response);
			}

			for (Map<String, Object> lessonMap : lessonData) {
				String title = (String) lessonMap.get("title");
				if (title == null || title.trim().isEmpty()) {
					response.put("status", "error");
					response.put("message", "Lesson title cannot be empty");
					return ResponseEntity.badRequest().body(response);
				}
				String activityId = (String) lessonMap.get("activityId");
				if (activityId == null || activityId.trim().isEmpty()) {
					response.put("status", "error");
					response.put("message", "Lesson activityId cannot be empty");
					return ResponseEntity.badRequest().body(response);
				}

				Lesson lesson = new Lesson();
				lesson.setTitle(title);
				lesson.setVideoUrl((String) lessonMap.get("videoUrl"));
				lesson.setActivityId(activityId);
				lesson.setCourse(course);
				lesson.setStatus("INACTIVE");
				lessonService.saveLesson(lesson);
				log.info("Saved lesson: {}", lesson.getTitle());
			}

			response.put("status", "success");
			response.put("message", "Lessons have been successfully added!");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error saving lessons: ", e);
			response.put("status", "error");
			response.put("message", "Error saving lessons: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/lessons/delete/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> deleteLesson(@PathVariable("id") Long id) {
		Map<String, String> response = new HashMap<>();
		try {
			Lesson lesson = playService.getLessonById(id);
			if (lesson == null) {
				response.put("status", "error");
				response.put("message", "Lesson not found");
				return ResponseEntity.badRequest().body(response);
			}

			Long courseId = lesson.getCourse().getId();
			lessonService.deleteLessonById(id);

			response.put("status", "success");
			response.put("message", "Lesson has been deleted successfully");
			response.put("courseId", courseId.toString());
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error deleting lesson: ", e);
			response.put("status", "error");
			response.put("message", "An error occurred while trying to delete the lesson: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/lessons/toggleLessonStatus/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> toggleLessonStatus(@PathVariable("id") Long lessonId,
			@ModelAttribute("lesson") Lesson lesson) {
		Map<String, String> response = new HashMap<>();
		try {
			Lesson existingLesson = playService.getLessonById(lessonId);
			if (existingLesson == null) {
				response.put("status", "error");
				response.put("message", "Lesson not found");
				return ResponseEntity.badRequest().body(response);
			}

			Long courseId = existingLesson.getCourse().getId();
			if ("ACTIVE".equals(existingLesson.getStatus())) {
				existingLesson.setStatus("INACTIVE");
			} else {
				existingLesson.setStatus("ACTIVE");
			}

			lessonService.saveLesson(existingLesson);
			response.put("status", "success");
			response.put("message", "Lesson has been toggleLessonStatus successfully");
			response.put("courseId", courseId.toString());
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error deleting lesson: ", e);
			response.put("status", "error");
			response.put("message",
					"An error occurred while trying to toggleLessonStatus the lesson: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	// ADMIN PARAMETERS MANAGER
	@GetMapping("/listParameters")
	public ModelAndView loadParametersListDiff() {
		ModelAndView mav = new ModelAndView("fragments/adminListParameters :: parametersContent");
		List<String> parameterKeyDiff = parameterService.getParamKeyDiff();
		List<Parameter> parameters = parameterService.getAllParams();
		mav.addObject("parameterKeyDiff", parameterKeyDiff);
		mav.addObject("parameters", parameters);
		log.debug("Distinct parameter keys: {}", parameterKeyDiff);
		log.debug("get all params: {}", parameters);
		return mav;
	}

	@GetMapping("/addParameterHandsontable")
	public ModelAndView showAddParameterHandsontablePage() {
		ModelAndView mav = new ModelAndView(
				"fragments/adminAddParametersHandsontable :: addParameterWithHandsontableContent");
		return mav;
	}

	@PostMapping("/saveParametersHandsontable")
	@Transactional
	public ResponseEntity<Map<String, String>> saveParametersHandsontable(
			@RequestBody List<Map<String, Object>> parameterData) {
		log.info("Received request to save parameters");
		Map<String, String> response = new HashMap<>();

		try {
			log.info("Received parameter data: {}", parameterData);

			if (parameterData == null || parameterData.isEmpty()) {
				response.put("status", "error");
				response.put("message", "No parameter data sent!");
				return ResponseEntity.badRequest().body(response);
			}

			for (Map<String, Object> parameterMap : parameterData) {
				String paramKey = (String) parameterMap.get("paramKey");
				String paramValue = (String) parameterMap.get("paramValue");

				// Validation
				if (paramKey == null || paramKey.trim().isEmpty()) {
					response.put("status", "error");
					response.put("message", "Key value cannot be empty!");
					return ResponseEntity.badRequest().body(response);
				}

				if (paramValue == null || paramValue.trim().isEmpty()) {
					response.put("status", "error");
					response.put("message", "The value cannot be empty!");
					return ResponseEntity.badRequest().body(response);
				}

				if (!parameterService.paramKeyExists(paramKey)) {
					response.put("status", "error");
					response.put("message",
							"paramKey '" + paramKey + "' is invalid or does not exist in the database.");
					return ResponseEntity.badRequest().body(response);
				}

				Parameter parameter = new Parameter();
				parameter.setParamKey(paramKey);
				parameter.setParamValue(paramValue);

				try {
					parameterService.saveParameters(parameter);
				} catch (Exception ex) {
					response.put("status", "error");
					response.put("message", "Error occurred while adding parameter: " + ex.getMessage());
					return ResponseEntity.badRequest().body(response);
				}
			}

			response.put("status", "success");
			response.put("message", "Parameters added successfully!");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error saving parameters: ", e);
			response.put("status", "error");
			response.put("message", "Error saving parameters: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/parameter/delete/{parameterId}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> deleteParameter(@PathVariable("parameterId") Long parameterId) {
		Map<String, String> response = new HashMap<>();

		try {
			Parameter parameter = parameterService.getParameterById(parameterId);

			if (parameter != null) {
				parameterService.deleteParameter(parameterId);
				response.put("status", "success");
				response.put("message", "Parameter was deleted successfully!");
				return ResponseEntity.ok(response);
			} else {
				response.put("status", "error");
				response.put("message", "Parameter does not exist.");
				return ResponseEntity.badRequest().body(response);
			}
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "Error occurred while deleting Parameter: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/parameter/restoreParameter/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> restoreParameter(@PathVariable("id") Long id,
			@ModelAttribute("parameter") Parameter parameter) {
		Map<String, String> response = new HashMap<>();
		try {
			Parameter existingParameter = parameterService.getParameterById(id);
			if (existingParameter == null) {
				response.put("status", "error");
				response.put("message", "Parameter not found");
				return ResponseEntity.badRequest().body(response);
			}

			existingParameter.setStatus("ACTIVE");
			parameterService.saveParameters(existingParameter);
			response.put("status", "success");
			response.put("message", "Parameter has been restore successfully");

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error deleting lesson: ", e);
			response.put("status", "error");
			response.put("message", "An error occurred while trying to restore the Parameter: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/parameter/edit/{id}")
	public ModelAndView showEditParameterForm(@PathVariable("id") Long id) {
		Parameter parameter = parameterService.getParameterById(id);
		if (parameter == null) {
			return new ModelAndView("redirect:/admin/listParameters");
		}

		ModelAndView mav = new ModelAndView("fragments/adminEditParameter :: editParameterModal");
		mav.addObject("parameter", parameter);
		mav.addObject("parametersKeyDistinct", parameterService.getAllDistinctParamKeys());
		return mav;
	}

	@PostMapping("/parameter/edit/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> editParameter(@PathVariable("id") Long id,
			@ModelAttribute("parameter") Parameter parameter) {
		Parameter existingParameter = parameterService.getParameterById(id);
		if (existingParameter == null) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Parameter not found"));
		}

		existingParameter.setParamValue(parameter.getParamValue());
		parameterService.saveParameters(existingParameter);

		return ResponseEntity.ok(Map.of("status", "success", "message", "Parameter updated successfully"));
	}
	@GetMapping("/listManageLearningProgress")
	public ModelAndView loadUserLibraryFragment() {
		ModelAndView mav = new ModelAndView("fragments/adminManageLearningProgress :: userLibraryContent");

		// Fetch all unique user EIDs from user library courses
		List<String> uniqueUserEids = userLibraryCourseService.findAllUniqueUserEids();

		// Prepare a list to hold user library information
		List<UserLibraryDTO> userLibraryData = new ArrayList<>();

		for (String userEid : uniqueUserEids) {
			// Get all courses for this user
			List<UserLibraryCourse> userCourses = userLibraryCourseService.getUserLibraryCoursesByUserEid(userEid);

			// Calculate completed and in-progress courses
			long completedCourses = userCourses.stream()
					.filter(course -> course.getProgressStatus() == UserLibraryCourse.ProgressStatus.COMPLETE).count();

			long inProgressCourses = userCourses.stream()
					.filter(course -> course.getProgressStatus() == UserLibraryCourse.ProgressStatus.IN_PROGRESS)
					.count();

			// Create DTO for this user
			UserLibraryDTO userDto = new UserLibraryDTO();
			userDto.setUserEid(userEid);
			userDto.setCompletedCoursesCount(completedCourses);
			userDto.setInProgressCoursesCount(inProgressCourses);
			userDto.setXapiLink("https://mksol.vn/xapi-lrs/" + userEid + "/course");

			userLibraryData.add(userDto);
		}

		mav.addObject("userLibraries", userLibraryData);
		return mav;
	}

	@Data
	public class UserLibraryDTO {
		private String userEid;
		private long completedCoursesCount;
		private long inProgressCoursesCount;
		private String xapiLink;
	}
}
