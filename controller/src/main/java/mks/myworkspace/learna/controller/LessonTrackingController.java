package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.service.LessonTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.Data;

@RestController
@RequestMapping("/api/lesson-tracking")
public class LessonTrackingController {

    private final LessonTrackingService lessonTrackingService;

    @Autowired
    public LessonTrackingController(LessonTrackingService lessonTrackingService) {
        this.lessonTrackingService = lessonTrackingService;
    }

    @PostMapping
    public ResponseEntity<?> saveTracking(@RequestBody TrackingRequest request) {
        try {
            lessonTrackingService.saveTracking(
                request.getUserEid(),
                request.getCourseId(),
                request.getLessonId(),
                request.getCourseUrl(),
                request.getActivityId()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to save tracking");
        }
    }
}

@Data
class TrackingRequest {
    private String userEid;
    private Long courseId;
    private Long lessonId;
    private String courseUrl;
    private String activityId;
}