package mks.myworkspace.learna.service;

import java.util.List;

import mks.myworkspace.learna.entity.Parameter;

public interface ParameterService {

	String getLogoUrl();
	List<Parameter> getListParamsByParamValue(String paramKey);
	Parameter getParameterById(Long id);
	Parameter getParameterByParamKeyAndParamValue(String paramKey, String paramValue);
}
