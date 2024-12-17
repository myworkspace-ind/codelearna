package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.Business;
import mks.myworkspace.learna.repository.BusinessRepository;
import mks.myworkspace.learna.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;

    @Autowired
    public BusinessServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    @Override
    public Business getBusinessById(Long id) {
        return businessRepository.findById(id).orElse(null);
    }

    @Override
    public Business saveBusiness(Business business) {
        return businessRepository.save(business);
    }

    @Override
    public void deleteBusinessById(Long id) {
        businessRepository.deleteById(id);
    }

    // Thêm các phương thức khác nếu cần
}