package mks.myworkspace.learna.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Parameter;
import mks.myworkspace.learna.repository.ParameterRepository;
import mks.myworkspace.learna.service.ParameterService;

@Service
public class ParameterServiceImpl implements ParameterService{
	
	@Autowired
	private ParameterRepository repo;
	
	@Override
	public String getLogoUrl() {
		Optional<Parameter> parameter = repo.findByParamKey("site_logo");
        return parameter.map(Parameter::getParamValue).orElse("logo");
	}

}
