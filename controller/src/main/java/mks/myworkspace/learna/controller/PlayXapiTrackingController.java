package mks.myworkspace.learna.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("demo")
public class PlayXapiTrackingController {

    @GetMapping("play-tracking")
    public ModelAndView demoPlayXapiLessonTracking() {
        ModelAndView mav = new ModelAndView("demo_play_xapi_tracking");

        return mav;
    }
}
