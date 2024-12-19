package mks.myworkspace.learna.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.service.VoucherService;

@Slf4j
@Controller
@RequestMapping("/voucher")
public class VoucherController {

	@Autowired
	private VoucherService voucherService;

	@GetMapping("/applyVoucher")
	public ModelAndView loadVouchersFragment() { 
	    ModelAndView mav = new ModelAndView("fragments/listVoucher :: vouchersContent");
	    mav.addObject("vouchers", voucherService.getAllVouchers());
	    return mav;
	}
	//Post
}
