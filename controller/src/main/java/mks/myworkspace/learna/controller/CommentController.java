package mks.myworkspace.learna.controller;


import mks.myworkspace.learna.entity.Comment;
import mks.myworkspace.learna.entity.Lesson;
import mks.myworkspace.learna.entity.User;
import mks.myworkspace.learna.repository.LessonRepository;
import mks.myworkspace.learna.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
/*import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;*/
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mks.myworkspace.learna.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CommentController extends BaseController {
	 private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

	
    @Autowired
    private CommentService commentService;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/play/{courseId}/{lessonId}/comments")
    @ResponseBody
    public String submitComment(
            @PathVariable Long lessonId,
            @RequestParam String content) {
        try {
            
            Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
            if (!lessonOpt.isPresent()) {
                return "Bài học không tồn tại!";
            }
            Lesson lesson = lessonOpt.get();
            String userId = getCurrentUserEid();
            logger.info("Submitting comment by userId: {}", userId);
            Comment newComment = new Comment();
            newComment.setContent(content);
            newComment.setLesson(lesson);
            newComment.setUserEid(userId);

         
            commentService.saveComment(newComment);

            return "Đã gửi bình luận thành công!";
        } catch (Exception e) {
            return "Lỗi khi gửi bình luận: " + e.getMessage();
        }
    }
    
    
    @PostMapping("/play/comments/{commentId}/reply")
    public ResponseEntity<String> saveReply(@PathVariable Long commentId, @RequestParam String content) {
        try {
         
            Comment parentComment = commentService.findById(commentId);
            if (parentComment == null) {
                return ResponseEntity.badRequest().body("Comment not found");
            }

            String userId = getCurrentUserEid();
            Comment reply = new Comment();
            reply.setContent(content);
            reply.setParentComment(parentComment); 
            reply.setLesson(parentComment.getLesson()); 
            reply.setUserEid(userId); 

           
            commentService.saveComment(reply);

            return ResponseEntity.ok("Reply saved successfully");
        } catch (Exception e) {
           
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing reply: " + e.getMessage());
        }
    }



}
