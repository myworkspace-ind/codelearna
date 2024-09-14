package mks.myworkspace.learna.controller;

import java.util.List;

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

@Controller
public class PlayController {

    @Autowired
    private PlayService playService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/play/{courseId}")
    public ModelAndView displayCourseLessons(
    		@PathVariable Long courseId, 
    	    @RequestParam(name = "page", defaultValue = "0") int page,  // Chỉ định tên của tham số
    	    HttpServletRequest request, 
    	    HttpSession session
    ) {
        ModelAndView mav = new ModelAndView("play");

        // Lấy danh sách bài học theo courseId
        List<Lesson> lessons = playService.getLessonsByCourseId(courseId);
        mav.addObject("lessons", lessons);

        // Kiểm tra nếu danh sách bài học không trống
        if (lessons != null && !lessons.isEmpty()) {
            // Lấy bài học đầu tiên và bình luận của nó
            Lesson firstLesson = lessons.get(0);

            // Sử dụng PageRequest để giới hạn số lượng comment trả về (10 bình luận/trang)
            var commentsPage = commentService.getCommentsByLessonIdAndParentCommentIsNull(firstLesson.getId(), PageRequest.of(page, 10));
            mav.addObject("commentsPage", commentsPage);

            // Truyền bài học đầu tiên như bài học hiện tại
            mav.addObject("currentLesson", firstLesson);
            mav.addObject("currentPage", commentsPage.getNumber());
            mav.addObject("totalPages", commentsPage.getTotalPages());
        } else {
            // Trường hợp không có bài học
            mav.addObject("currentLesson", null);
            mav.addObject("commentsPage", null);
        }

        return mav;
    }
    
    @GetMapping("/comments")
    @ResponseBody
    public Page<Comment> getComments(
        @RequestParam Long lessonId, 
        @RequestParam int page, 
        @RequestParam int size
    ) {
        // Trả về các bình luận phân trang
        return commentService.getCommentsByLessonIdAndParentCommentIsNull(lessonId, PageRequest.of(page, size));
    }

}
