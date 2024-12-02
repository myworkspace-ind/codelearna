package mks.myworkspace.learna.service;

import java.util.List;

import mks.myworkspace.learna.entity.Parameter;

public interface ParameterService {

	String getLogoUrl();
	List<Parameter> getAllParams();
	List<Parameter> getListParamsByParamValue(String paramKey);
	Parameter getParameterById(Long id);
	Parameter getParameterByParamKeyAndParamValue(String paramKey, String paramValue);
	List<String> getAllDistinctParamKeys();
	Parameter saveParameters(Parameter parameters);
	boolean paramKeyExists(String paramKey);
	void deleteParameter(Long id);
	Parameter addParamValueToParamKey(Parameter parameter);
	Parameter saveCategoryAndSubCategory(Parameter parameter, Long id);
}
