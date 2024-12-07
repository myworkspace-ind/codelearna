package mks.myworkspace.learna.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.repository.UserLibraryCourseRepository;
import mks.myworkspace.learna.service.RevenueService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RevenueServiceImpl implements RevenueService {

    private final UserLibraryCourseRepository userLibraryCourseRepository;

    @Autowired
    public RevenueServiceImpl(UserLibraryCourseRepository userLibraryCourseRepository) {
        this.userLibraryCourseRepository = userLibraryCourseRepository;
    }

    @Override
    public Double getTotalRevenue() {
        return userLibraryCourseRepository.calculateTotalRevenue();
    }

    @Override
    public Map<String, Object> getRevenueStatistics(int page, int size, String timePeriod, Date startDate, Date endDate) {
        Pageable pageable = PageRequest.of(page - 1, size); // Page starts from 0
        Page<Object[]> pageResults;

        // Calculate startDate and endDate based on timePeriod if not custom
        if (!"custom".equalsIgnoreCase(timePeriod)) {
            endDate = new Date(); // Current date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);

            switch (timePeriod.toLowerCase()) {
                case "day":
                    startDate = endDate; // Today
                    break;
                case "month":
                    calendar.add(Calendar.MONTH, -1);
                    startDate = calendar.getTime();
                    break;
                case "3month":
                    calendar.add(Calendar.MONTH, -3);
                    startDate = calendar.getTime();
                    break;
                case "6month":
                    calendar.add(Calendar.MONTH, -6);
                    startDate = calendar.getTime();
                    break;
                case "year":
                    calendar.add(Calendar.YEAR, -1);
                    startDate = calendar.getTime();
                    break;
                default:
                    startDate = null;
                    endDate = null;
                    break;
            }
        }

        log.debug("Calculated startDate: {} and endDate: {}", startDate, endDate);

        if (startDate != null && endDate != null) {
            // Use a single repository method with startDate and endDate for all cases
            pageResults = userLibraryCourseRepository.calculateRevenueByDateRange(startDate, endDate, pageable);
        } else {
            // Default to all-time revenue if no specific range is provided
            pageResults = userLibraryCourseRepository.calculateRevenueByCourse(pageable);
        }

        List<Map<String, Object>> revenueData = new ArrayList<>();
        for (Object[] result : pageResults.getContent()) {
            Map<String, Object> map = new HashMap<>();
            map.put("courseId", result[0]);
            map.put("courseName", result[1]);
            map.put("purchaseCount", result[2]);
            map.put("revenue", result[3]);
            revenueData.add(map);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", getTotalRevenue());
        response.put("revenueByCourse", revenueData);
        response.put("totalPages", pageResults.getTotalPages());
        response.put("currentPage", pageResults.getNumber() + 1);

        return response;
    }

}
