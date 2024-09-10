package mks.myworkspace.learna.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.service.PlayService;
import mks.myworkspace.learna.service.CommentService;

@Controller
public class PlayController {

    @Autowired
    private PlayService playService;

    @Autowired
    private CommentService commentService;  

    @GetMapping("/play/{courseId}")
    public ModelAndView displayCourseLessons(@PathVariable Long courseId, HttpServletRequest request, HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("play");
        
      
        List<Lesson> lessons = playService.getLessonsByCourseId(courseId);
        mav.addObject("lessons", lessons); 

 
        Map<Long, List<Comment>> commentsByLesson = lessons.stream()
            .collect(Collectors.toMap(
                Lesson::getId, 
                lesson -> commentService.getCommentsByLessonId(lesson.getId())
            ));
        mav.addObject("commentsByLesson", commentsByLesson);

        Object currentSiteId = request.getSession().getAttribute("currentSiteId");
        if (currentSiteId != null) {
            mav.addObject("currentSiteId", currentSiteId);
        } else {
            mav.addObject("currentSiteId", "Not Set");
        }

        if (request.getUserPrincipal() != null) {
            mav.addObject("userDisplayName", request.getUserPrincipal().getName());
        } else {
            mav.addObject("userDisplayName", "Guest");
        }

        return mav;
    }
}
