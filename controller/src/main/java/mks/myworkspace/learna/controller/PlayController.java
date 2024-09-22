package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.service.CommentService;
import mks.myworkspace.learna.service.PlayService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
public class PlayController {

    @Autowired
    private PlayService playService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/play/{courseId}")
    public ModelAndView displayCourseLessons(
            @PathVariable Long courseId,
            @RequestParam(required = false) Long lessonId) {
        ModelAndView mav = new ModelAndView("play");

        // Lấy danh sách các bài học thuộc khóa học
        List<Lesson> lessons = playService.getLessonsByCourseId(courseId);
        mav.addObject("lessons", lessons);

        // Xác định bài học hiện tại dựa vào lessonId từ URL
        Lesson currentLesson = lessons.stream()
                .filter(l -> lessonId == null || l.getId().equals(lessonId))
                .findFirst()
                .orElse(lessons.isEmpty() ? null : lessons.get(0));

        // Khởi tạo các thuộc tính của bài học để tránh lỗi proxy
        if (currentLesson != null) {
            Hibernate.initialize(currentLesson.getComments());
            currentLesson.getComments().forEach(comment -> {
                Hibernate.initialize(comment.getUser());
                initializeChildComments(comment);
            });

            // Lấy bình luận đúng theo bài học hiện tại
            List<Comment> comments = commentService.getCommentsByLessonIdAndParentCommentIsNull(currentLesson.getId());
            mav.addObject("comments", comments);
        }

        mav.addObject("currentLesson", currentLesson);
        return mav;
    }

    // Hàm khởi tạo các bình luận con (child comments)
    private void initializeChildComments(Comment comment) {
        Hibernate.initialize(comment.getChildComments());
        comment.getChildComments().forEach(this::initializeChildComments);
    }
    
    @GetMapping("/play/{courseId}/{lessonId}/comments")
    public ModelAndView loadCommentsForLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId) {
        ModelAndView mav = new ModelAndView("fragments/commentList");

        List<Comment> comments = commentService.getCommentsByLessonIdAndParentCommentIsNull(lessonId);
        log.info("Đang trả về {} bình luận cho bài học {}", comments.size(), lessonId);
        mav.addObject("comments", comments);
        
        return mav; // Trả về fragment
    }

    
    

}
