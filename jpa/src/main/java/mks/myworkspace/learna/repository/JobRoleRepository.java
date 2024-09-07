package mks.myworkspace.learna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.JobRole;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, Long> {
}
