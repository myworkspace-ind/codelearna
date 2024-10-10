package mks.myworkspace.learna.controller;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

/*import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;*/
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
import mks.myworkspace.learna.service.ReviewService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {

    @Autowired
    private CourseService courseService;
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private CampaignService campaignService;
    
	/*
	 * @Autowired private UserDirectoryService userDirectoryService;
	 */
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Sample init of Custom Editor

//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

    }

    @GetMapping
    public ModelAndView getAllCourses() {
		ModelAndView mav = new ModelAndView("homePage");
//        User currentUser = userDirectoryService.getCurrentUser();
//		log.info("Thông tin người dùng: {}", currentUser);
//		
        List<Course> courses = courseService.getAllCourses();
        mav.addObject("courses", courses);

        List<Course> featuredCourses = courseService.getRandomCourses();
        mav.addObject("featuredCourses", featuredCourses);
        
        List<Campaign> campaigns = campaignService.getAllCampaigns();
        
        // Lọc các campaign theo ngày bắt đầu và ngày kết thúc
        Date today = new Date();
        campaigns = campaigns.stream()
            .filter(campaign -> !campaign.getEndTime().before(today) && !campaign.getStartTime().after(today))
            .collect(Collectors.toList());
        
        mav.addObject("campaigns", campaigns);
        

        return mav;
    }
}