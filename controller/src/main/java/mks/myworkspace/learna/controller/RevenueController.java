package mks.myworkspace.learna.controller;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Campaign;
import mks.myworkspace.learna.service.CampaignService;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.PaymentService;
import mks.myworkspace.learna.service.RevenueService;
import mks.myworkspace.learna.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping
    public ModelAndView getRevenuePage() {
        ModelAndView mav = new ModelAndView("revenue");
        return mav;
    }
}


