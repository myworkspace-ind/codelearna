package mks.myworkspace.learna.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.ParameterService;

@Controller
@RequestMapping("/myadmin")
@Slf4j
public class MyAdminController extends BaseController {
	@Autowired
	private ParameterService parameterService;
	
	@Autowired
	private CourseService courseService;
	@GetMapping("")
	public String showSettings(HttpServletRequest request, HttpSession httpSession) {
		super.initSession(request, httpSession);
		
		httpSession.setAttribute("currentMenu", "Settings");
		return "myadmin";
	}

	@GetMapping("/current-users")
	public String showCurrentUser(HttpServletRequest request, HttpSession httpSession) {
		
		httpSession.setAttribute("currentMenu", "CurrentUsers");
		return "myadmin";
	}

	@GetMapping("/list-parameters")
	public String loadParametersList(HttpServletRequest request, HttpSession httpSession, Model model) {
	    httpSession.setAttribute("currentMenu", "Parameters");
	    model.addAttribute("parameters", parameterService.getAllParams()); 
	    return "myadmin";
	}

	@GetMapping("/course-management")
	public String loadCourseList(HttpServletRequest request, HttpSession httpSession, Model model) {
	    httpSession.setAttribute("currentMenu", "Course"); 
	    model.addAttribute("courses", courseService.getAllCourses()); 

	    return "myadmin";
	}
}