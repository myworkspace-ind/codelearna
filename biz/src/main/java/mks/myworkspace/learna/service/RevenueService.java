package mks.myworkspace.learna.service;

import java.util.Date;
import java.util.Map;

public interface RevenueService {
    Double getTotalRevenue();
    Map<String, Object> getRevenueStatistics(int page, int size, String timePeriod, Date startDate, Date endDate);
}
