package mks.myworkspace.learna.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.service.CommentService;
import mks.myworkspace.learna.service.PlayService;


import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class PlayController {
    @Autowired
    private PlayService playService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/play/{courseId}")
    public ModelAndView displayCourseLessons(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            HttpServletRequest request,
            HttpSession session
    ) {
        ModelAndView mav = new ModelAndView("play");
        if (page < 0) {
            page = 0;
        }

        List<Lesson> lessons = playService.getLessonsByCourseId(courseId);
        mav.addObject("lessons", lessons);

        if (lessons != null && !lessons.isEmpty()) {
            Lesson currentLesson = lessons.get(0);
            Pageable pageable = PageRequest.of(page, 10);
            Page<Comment> commentsPage = commentService.getCommentsByLessonIdAndParentCommentIsNull(currentLesson.getId(), pageable);

            mav.addObject("currentLesson", currentLesson);
            mav.addObject("commentsPage", commentsPage);
            mav.addObject("currentPage", commentsPage.getNumber());
            mav.addObject("totalPages", commentsPage.getTotalPages());
        } else {
            mav.addObject("currentLesson", null);
            mav.addObject("commentsPage", null);
        }

        return mav;
    }


    
    @GetMapping("/play/{courseId}/comments")
    @ResponseBody
    public ResponseEntity<?> getComments(@PathVariable Long courseId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentService.getCommentsByLessonIdAndParentCommentIsNull(courseId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("comments", commentsPage.getContent());
        response.put("currentPage", commentsPage.getNumber());
        response.put("totalPages", commentsPage.getTotalPages());
        response.put("totalItems", commentsPage.getTotalElements());

        return ResponseEntity.ok(response);
    }
}