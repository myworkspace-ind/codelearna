package mks.myworkspace.learna.controller;

import java.util.List;
import java.util.Date; // Thêm import này
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

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {

    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CampaignService campaignService;
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Sample init of Custom Editor

//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

    }

    @GetMapping
    public ModelAndView getAllCourses() {
		ModelAndView mav = new ModelAndView("home");

        List<Course> courses = courseService.getAllCourses();
        mav.addObject("courses", courses);
        log.debug("Danh sách khóa học: {}", courses);

        List<Course> featuredCourses = courseService.getRandomCourses();
        mav.addObject("featuredCourses", featuredCourses);
        log.debug("Khóa học nổi bật: {}", featuredCourses);
        
        List<Campaign> campaigns = campaignService.getAllCampaigns();
        // Tạo danh sách campaignDTO để chứa thông tin và progress
        List<Map<String, Object>> campaignDTOs = new ArrayList<>();
        
        campaigns.forEach(campaign -> {
            long totalDays = (campaign.getEndTime().getTime() - campaign.getStartTime().getTime()) / (1000 * 60 * 60 * 24);
            long remainingDays = (campaign.getEndTime().getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
            double progress = (double) remainingDays / totalDays * 100;

            // Tạo map để chứa thông tin campaign và progress
            Map<String, Object> campaignData = new HashMap<>();
            campaignData.put("campaign", campaign);
            campaignData.put("progress", progress);
            campaignDTOs.add(campaignData);
        });

        mav.addObject("campaigns", campaignDTOs); // Truyền danh sách campaignDTO vào mô hình
        log.debug("Danh sách chương trình khuyến mãi: {}", campaignDTOs);

        // Thêm dòng này để truyền currentDate vào mô hình
        mav.addObject("currentDate", new Date());

        return mav;
    }
}