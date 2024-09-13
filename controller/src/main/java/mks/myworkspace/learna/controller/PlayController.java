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
    public ModelAndView displayCourseLessons(@PathVariable Long courseId, HttpServletRequest request, HttpSession session) {
        ModelAndView mav = new ModelAndView("play");
        
        // Get lessons by course ID
        List<Lesson> lessons = playService.getLessonsByCourseId(courseId);
        mav.addObject("lessons", lessons); 

        // Get the first lesson's comments as default
        Lesson firstLesson = lessons.get(0);
        List<Comment> commentsForFirstLesson = commentService.getCommentsByLessonId(firstLesson.getId());
        mav.addObject("commentsForLesson", commentsForFirstLesson);

        // Pass the first lesson as the currently playing lesson
        mav.addObject("currentLesson", firstLesson);

        return mav;
    }


}
