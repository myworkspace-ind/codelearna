package mks.myworkspace.learna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.SakaiUserIdMap;

@Repository
public interface SakaiUserIdMapRepository extends JpaRepository<SakaiUserIdMap, String> {
	
}
