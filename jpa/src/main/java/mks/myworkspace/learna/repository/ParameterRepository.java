package mks.myworkspace.learna.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mks.myworkspace.learna.entity.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

	Optional<Parameter> findByParamKey(String paramKey);
}
