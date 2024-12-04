package mks.myworkspace.learna.service;

import java.util.List;
import java.util.Optional;

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
	List<String> getParamKeyDiff();
	List<Parameter> getParamValues(String paramKey);
}
