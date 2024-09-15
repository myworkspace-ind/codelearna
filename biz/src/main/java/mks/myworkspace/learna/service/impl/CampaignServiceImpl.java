package mks.myworkspace.learna.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mks.myworkspace.learna.entity.Campaign;
import mks.myworkspace.learna.repository.CampaignRepository;
import mks.myworkspace.learna.service.CampaignService;

@Service
public class CampaignServiceImpl implements CampaignService {

    @Autowired
    private CampaignRepository repo;

    @Override
    public CampaignRepository getRepo() {
        return repo;
    }

    @Override
    public Campaign saveCampaign(Campaign campaign) {
        return repo.save(campaign);
    }

    @Override
    public Campaign getCampaignById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteCampaign(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Campaign> getAllCampaigns() {
        return repo.findAll();
    }
}