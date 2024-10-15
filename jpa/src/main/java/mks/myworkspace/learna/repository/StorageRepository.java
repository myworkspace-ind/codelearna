package mks.myworkspace.learna.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Parameter;

@Slf4j
@Repository
public class StorageRepository {
	@Autowired
	@Qualifier("jdbcTemplate0")
	private JdbcTemplate jdbcTemplate0;
	
	@Transactional
	public List<Long> saveParameter(List<Parameter> entities) {
		List<Long> ids = new ArrayList<Long>();
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("learna_parameter").usingGeneratedKeyColumns("id");
		
		Long id;
		for (Parameter e : entities) {
		    
			if (e.getId() == null) {
				id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(e)).longValue();
			} else {
				// Update
				// updateParameterByKey(e);
				updateParameterByKey(e);
				
				// Or Update by Id
				// updateParameter(e);
				id = e.getId();
			}

			ids.add(id);
		}
		
		return ids;
	}
	
	/**
	 * Update parameter bi Id
	 * @param e
	 * @return
	 */
	public int updateParameter(Parameter e) {
		String sql = "UPDATE learna_parameter SET param_value=?, description=? WHERE id=?";
		
		return jdbcTemplate0.update(sql,
				e.getParamValue(),
				e.getDescription(),
				e.getId());
	}

	public int updateParameterByKey(Parameter e) {
		String sql = "UPDATE learna_parameter SET param_value=?, description=? WHERE param_key=?";
		
		return jdbcTemplate0.update(sql,
				e.getParamValue(),
				e.getDescription(),
				e.getParamKey());
	}
}
