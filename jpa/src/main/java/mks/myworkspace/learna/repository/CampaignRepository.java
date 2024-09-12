package mks.myworkspace.learna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mks.myworkspace.learna.entity.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}