package mks.myworkspace.codelearna.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.myworkspace.codelearna.repository.JobRoleRepository;
import mks.myworkspace.codelearna.service.JobRoleService;

@Service
public class JobRoleServiceImpl implements JobRoleService {

	@Getter
	@Autowired
	JobRoleRepository repo;

}