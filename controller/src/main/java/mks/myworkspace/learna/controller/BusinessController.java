package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Business;
import mks.myworkspace.learna.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/business")
public class BusinessController extends BaseController {

    @Autowired
    private BusinessService businessService;

    @GetMapping
    public ModelAndView getBusinesses(HttpServletRequest request, HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("business");
        initSession(request, httpSession);

        // Lấy danh sách các doanh nghiệp
        List<Business> businesses = businessService.getAllBusinesses();
        mav.addObject("businesses", businesses);

        return mav;
    }
    
    @GetMapping("/add")
    public ModelAndView addBusinessForm(HttpServletRequest request, HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("businessAdd"); // Trả về view cho trang thêm doanh nghiệp
        initSession(request, httpSession);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getBusinessDetails(@PathVariable("id") Long businessId) {
        ModelAndView mav = new ModelAndView("businessDetails");

        // Lấy thông tin chi tiết của một doanh nghiệp
        Business business = businessService.getBusinessById(businessId);
        mav.addObject("business", business);

        return mav;
    }

    @PostMapping("/save")
    public String saveBusiness(@ModelAttribute("business") Business business) {
        businessService.saveBusiness(business);
        return "redirect:/business";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editBusiness(@PathVariable("id") Long businessId) {
        ModelAndView mav = new ModelAndView("businessEdit");

        // Lấy thông tin của một doanh nghiệp để hiển thị trên form chỉnh sửa
        Business business = businessService.getBusinessById(businessId);
        mav.addObject("business", business);

        return mav;
    }


    @GetMapping("/delete/{id}")
    public String deleteBusiness(@PathVariable("id") Long businessId) {
        businessService.deleteBusinessById(businessId);
        return "redirect:/business";
    }
}
