package mks.myworkspace.learna.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mks.myworkspace.learna.entity.Campaign;
import mks.myworkspace.learna.repository.CampaignRepository;
import mks.myworkspace.learna.repository.CampaignJdbcRepository;
import mks.myworkspace.learna.service.CampaignService;

@Service
public class CampaignServiceImpl implements CampaignService {
    
    @Autowired
    private CampaignRepository repo;
    
    @Autowired
    private CampaignJdbcRepository jdbcRepo;

    @Override
    public CampaignRepository getRepo() {
        return repo;
    }

    @Override
    public Campaign saveCampaign(Campaign campaign) {
        // Using JDBC repository for save operation
        return jdbcRepo.save(campaign);
    }

    @Override
    public Campaign getCampaignById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteCampaign(Long id) {
        // Using JDBC repository for soft delete
        jdbcRepo.deleteById(id);
    }

    @Override
    public List<Campaign> getAllCampaigns() {
        return repo.findAll();
    }

    @Override
    public void updateCampaignStatus(Long id, String status) {
        Campaign campaign = repo.findById(id).orElse(null);
        if (campaign != null) {
            campaign.setStatus(status);
            // Using JDBC repository for update operation
            jdbcRepo.save(campaign);
        }
    }
}