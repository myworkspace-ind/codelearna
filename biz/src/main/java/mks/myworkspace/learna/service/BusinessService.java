package mks.myworkspace.learna.service;

import mks.myworkspace.learna.entity.Business;
import java.util.List;

public interface BusinessService {
    List<Business> getAllBusinesses();
    Business getBusinessById(Long id);
    Business saveBusiness(Business business);
    void deleteBusinessById(Long id);
    // Thêm các phương thức khác nếu cần
}