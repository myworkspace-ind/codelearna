package mks.myworkspace.learna.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.service.PlayService;

@Controller
public class PlayController {

    @Autowired
    private PlayService playService;

    @GetMapping("/play/{courseId}")
    public ModelAndView displayCourseLessons(@PathVariable Long courseId, HttpServletRequest request, HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("play");
        
        List<Lesson> lessons = playService.getLessonsByCourseId(courseId);
        mav.addObject("lessons", lessons); // Luôn luôn thêm lessons, kể cả khi nó rỗng
        
        // Kiểm tra và thêm thuộc tính currentSiteId nếu tồn tại
        Object currentSiteId = request.getSession().getAttribute("currentSiteId");
        if (currentSiteId != null) {
            mav.addObject("currentSiteId", currentSiteId);
        } else {
            mav.addObject("currentSiteId", "Not Set"); // Hoặc bạn có thể set một giá trị mặc định
        }
        
        // Kiểm tra và thêm userDisplayName nếu người dùng đã đăng nhập
        if (request.getUserPrincipal() != null) {
            mav.addObject("userDisplayName", request.getUserPrincipal().getName());
        } else {
            mav.addObject("userDisplayName", "Guest"); // Hoặc bạn có thể set một giá trị mặc định
        }

        return mav;
    }
}
