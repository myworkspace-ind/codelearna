package mks.myworkspace.learna.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/myadmin")
@Slf4j
public class MyAdminController extends BaseController {

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
}