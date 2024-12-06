package mks.myworkspace.learna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.SakaiUser;

@Repository
public interface SakaiUserRepository extends JpaRepository<SakaiUser, String> {
}
