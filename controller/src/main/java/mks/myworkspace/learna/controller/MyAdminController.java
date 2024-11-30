package mks.myworkspace.learna.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Parameter;
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
	    model.addAttribute("parameterKeyDiff", parameterService.getParamKeyDiff()); 
	    return "myadmin";
	}

	@GetMapping("/course-management")
	public String loadCourseList(HttpServletRequest request, HttpSession httpSession, Model model) {
	    httpSession.setAttribute("currentMenu", "Course"); 
	    model.addAttribute("courses", courseService.getAllCourses()); 
	    return "myadmin";
	}
	@GetMapping("/revenue-statistics")
	public String loadRevenueStatistics(HttpServletRequest request, HttpSession httpSession, Model model) {
	    httpSession.setAttribute("currentMenu", "RevenueStatistics"); 
	    // model.addAttribute("courses", courseService.getAllCourses()); 
	    return "myadmin";
	}
	
	@GetMapping("/search-parameters")
	public String searchParameters(HttpServletRequest request, HttpSession httpSession, Model model) {
		String parameter = request.getParameter("parameter");
	    httpSession.setAttribute("currentMenu", "Parameters");
	    model.addAttribute("parameters", parameterService.getListParamsByParamValue(parameter)); 
	    model.addAttribute("parameterKeyDiff", parameterService.getParamKeyDiff()); 
	    return "myadmin";
	}
}