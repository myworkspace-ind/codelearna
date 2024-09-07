package mks.myworkspace.learna.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.myworkspace.learna.repository.JobRoleRepository;
import mks.myworkspace.learna.service.JobRoleService;

@Service
public class JobRoleServiceImpl implements JobRoleService {

	@Getter
	@Autowired
	JobRoleRepository repo;

}