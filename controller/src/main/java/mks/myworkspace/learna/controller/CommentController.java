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


@Controller
public class CommentController {

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
            // Lấy bài học từ lessonId
            Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
            if (!lessonOpt.isPresent()) {
                return "Bài học không tồn tại!";
            }
            Lesson lesson = lessonOpt.get();

            // Lấy user có id cố định là 1
            Optional<User> userOpt = userRepository.findById(1L); // user_id cố định là 1
            if (!userOpt.isPresent()) {
                return "Người dùng không tồn tại!";
            }
            User user = userOpt.get();

            // Tạo comment mới
            Comment newComment = new Comment();
            newComment.setContent(content);
            newComment.setLesson(lesson);
            newComment.setUser(user);

         
            commentService.saveComment(newComment);

            return "Đã gửi bình luận thành công!";
        } catch (Exception e) {
            return "Lỗi khi gửi bình luận: " + e.getMessage();
        }
    }
    
    
    @PostMapping("/play/comments/{commentId}/reply")
    public ResponseEntity<String> saveReply(@PathVariable Long commentId, @RequestParam String content) {
        try {
            // Tìm bình luận cha theo commentId
            Comment parentComment = commentService.findById(commentId);
            if (parentComment == null) {
                return ResponseEntity.badRequest().body("Comment not found");
            }

            // Tìm user có user_id là 1
            Optional<User> optionalUser = userRepository.findById(1L);
            if (!optionalUser.isPresent()) {
                return ResponseEntity.badRequest().body("User not found");
            }
            User currentUser = optionalUser.get();

            // Tạo bình luận con
            Comment reply = new Comment();
            reply.setContent(content);
            reply.setParentComment(parentComment); 
            reply.setLesson(parentComment.getLesson()); 
            reply.setUser(currentUser); 

           
            commentService.saveComment(reply);

            return ResponseEntity.ok("Reply saved successfully");
        } catch (Exception e) {
           
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing reply: " + e.getMessage());
        }
    }



}
