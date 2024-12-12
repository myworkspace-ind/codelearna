package mks.myworkspace.learna.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mks.myworkspace.learna.service.RevenueService;

@RestController
@RequestMapping("/api/revenue")
@RequiredArgsConstructor
public class RevenueApiController {

    private final RevenueService revenueService;

    @GetMapping("/statistics")
    public Map<String, Object> getRevenueStatistics(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "timePeriod", defaultValue = "all") String timePeriod,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return revenueService.getRevenueStatistics(page, size, timePeriod, startDate, endDate);
    }
}

