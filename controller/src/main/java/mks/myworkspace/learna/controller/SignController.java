package mks.myworkspace.learna.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mks.myworkspace.learna.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mks.myworkspace.learna.controller.model.UserDTO;
import mks.myworkspace.learna.service.UserService;
import mks.myworkspace.learna.service.impl.Pbkdf2PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;

@Controller
public class SignController extends BaseController {
	@Value("${user.register:0}")
    private int userRegister; 
    @Autowired
    private UserService userService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Pbkdf2PasswordEncoder passwordEncoder1 = new Pbkdf2PasswordEncoder();
    // Login page
////    @GetMapping("/login")
////    public ModelAndView showLoginPage(
////            @RequestParam(value = "error", required = false) String error,
////            @RequestParam(value = "logout", required = false) String logout) {
////        
////        ModelAndView model = new ModelAndView();
////        
////        if (error != null) {
////            model.addObject("error", "Email hoặc mật khẩu không chính xác!");
////        }
////
////        if (logout != null) {
////            model.addObject("message", "Bạn đã đăng xuất thành công!");
////        }
////
////        model.setViewName("/signInOut/signin");
////        return model;
////    }
//
//    // Logout
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return "redirect:/home";
//    }

    // Show register form
    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        ModelAndView mav = new ModelAndView("/signInOut/signup");
        return mav;
    }

    // Process registration
    @PostMapping("/register")
    public ModelAndView processRegistration(@ModelAttribute UserDTO userDTO, 
            BindingResult result, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();

        try {
            if (userRegister == 1) {
                // Nếu `user.register = 1`, sử dụng phương thức đăng ký vào Sakai
                String encodedPassword = passwordEncoder1.encode(userDTO.getPassword());
                String prefixedPassword = "PBKDF2:" + encodedPassword;

                userService.registerUserInSakai(userDTO.getFullName(), userDTO.getEmail(), prefixedPassword, userDTO.getPhone());
                redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
                mav.setViewName("redirect:/login");
            } 
//            else {
//                // Nếu không có key `user.register = 1`, sử dụng phương thức đăng ký mặc định
//                if (userService.isEmailExists(userDTO.getEmail())) {
//                    mav.addObject("error", "Email đã tồn tại trong hệ thống");
//                    mav.setViewName("/signInOut/signup");
//                    return mav;
//                }
//
//                if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
//                    mav.addObject("error", "Mật khẩu xác nhận không khớp");
//                    mav.setViewName("/signInOut/signup");
//                    return mav;
//                }

                User user = userService.createUser(userDTO.getFullName(), userDTO.getEmail(), 
                        passwordEncoder.encode(userDTO.getPassword()), userDTO.getPhone());
                redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
                mav.setViewName("redirect:/login");
//            }
        } catch (Exception e) {
            mav.addObject("error", e.getMessage());
//            mav.setViewName("/signInOut/signup");
        }

        return mav;
    }



    // Helper method to get current logged in user
    protected User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return userService.findUserByEmail(auth.getName());
        }
        return null;
    }
}