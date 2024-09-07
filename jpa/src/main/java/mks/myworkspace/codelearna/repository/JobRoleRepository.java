package mks.myworkspace.codelearna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.assistant.codelearna.entity.JobRole;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, Long> {
}
