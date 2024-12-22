package mks.myworkspace.learna.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mks.myworkspace.learna.entity.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
	/* @Modifying
    @Transactional
    @Query("DELETE FROM Campaign c WHERE c.id = :id")
    void deleteCampaignById(@Param("id") Long id); */
    @Transactional
    @Modifying
    @Query("UPDATE Campaign c SET c.status = 'DELETED' WHERE c.id = :id")
    void deleteCampaignById(@Param("id") Long id);
}