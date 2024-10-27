package mks.myworkspace.learna.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mks.myworkspace.learna.entity.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

	Optional<Parameter> findByParamKey(String paramKey);
	Parameter findByParamValue(String paramValue);
	
	
	@Query("SELECT p FROM Parameter p WHERE p.paramKey = :paramKey")
    List<Parameter> listByParamKey(@Param("paramKey") String paramKey);
	
	Parameter findByParamKeyAndParamValue(String paramKey, String paramValue);
	
	 @Query("SELECT DISTINCT p.paramKey FROM Parameter p WHERE p.paramKey <> 'site_logo'")
	    List<String> findDistinctParamKeys();
	 
	 boolean existsByParamKey(String paramKey);
}
