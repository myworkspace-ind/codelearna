package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.service.CommentService;
import mks.myworkspace.learna.service.PlayService;
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

        return mav;
    }

    @GetMapping("/play/{courseId}/{lessonId}/comments")
    public ModelAndView loadCommentsForLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ModelAndView mav = new ModelAndView("fragments/commentList");

        List<Comment> allComments = commentService.getCommentsByLessonIdAndParentCommentIsNull(lessonId);
        int totalComments = allComments.size();
        int totalPages = (int) Math.ceil((double) totalComments / size);

        int start = page * size;
        int end = Math.min((page + 1) * size, totalComments);
        List<Comment> paginatedComments = allComments.subList(start, end);

        log.info("Đang trả về {} bình luận cho bài học {} (trang {} / {})", 
                 paginatedComments.size(), lessonId, page + 1, totalPages);

        mav.addObject("comments", paginatedComments);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);

        return mav;
    }

    
    

}
