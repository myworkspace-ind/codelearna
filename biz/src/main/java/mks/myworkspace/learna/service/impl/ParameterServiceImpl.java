package mks.myworkspace.learna.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Parameter;
import mks.myworkspace.learna.repository.ParameterJdbcRepository;
import mks.myworkspace.learna.repository.ParameterRepository;
import mks.myworkspace.learna.service.ParameterService;

@Service
public class ParameterServiceImpl implements ParameterService {

	@Autowired
	private ParameterRepository repo;
	
	@Autowired
	private ParameterJdbcRepository parameterJdbcRepository;

	@Override
	public String getLogoUrl() {
		Optional<Parameter> parameter = repo.findByParamKey("site_logo");
		return parameter.map(Parameter::getParamValue).orElse("logo");
	}

	@Override
	public List<String> getAllDistinctParamKeys() {
		return repo.findDistinctParamKeys();
	}
	
	@Override
	public List<Parameter> getAllParams() {
		return repo.findAll();
	}
	
	@Override
	public Parameter saveParameters(Parameter parameters) {
		return parameterJdbcRepository.save(parameters);
	}

	@Override
	public List<Parameter> getListParamsByParamValue(String paramKey) {
		return repo.listByParamKey(paramKey);
	}

	@Override
	public Parameter getParameterById(Long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public Parameter getParameterByParamKeyAndParamValue(String paramKey, String paramValue) {
		return repo.findByParamKeyAndParamValue(paramKey, paramValue);
	}
	
	@Override
	 public boolean paramKeyExists(String paramKey) {
        return repo.existsByParamKey(paramKey);
    }
	
	@Override
	public void deleteParameter(Long id) {
		parameterJdbcRepository.deleteById(id);
	}
	@Override
	public List<String> getParamKeyDiff() {
		return parameterJdbcRepository.getParamKeyDiff();
	}

	
	@Override
	public Parameter addParamValueToParamKey(String paramKey, String paramValue) {
		return repo.findByParamKeyAndParamValue(paramKey, paramValue);
	}
}
