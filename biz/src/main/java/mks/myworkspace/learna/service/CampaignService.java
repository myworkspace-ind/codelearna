package mks.myworkspace.learna.service;

import java.util.List;
import mks.myworkspace.learna.entity.Campaign;
import mks.myworkspace.learna.repository.CampaignRepository;

public interface CampaignService {
    CampaignRepository getRepo();
    Campaign saveCampaign(Campaign campaign);
    Campaign getCampaignById(Long id);
    void deleteCampaign(Long id);
    List<Campaign> getAllCampaigns();
}