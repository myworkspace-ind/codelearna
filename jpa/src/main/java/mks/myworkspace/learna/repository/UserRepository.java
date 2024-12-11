package mks.myworkspace.learna.repository;


import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	User findUserByEmail(@Param("email")String email);
	
	@Query("SELECT u FROM User u WHERE u.createdDate >= :startOfPeriod AND u.role = 'ROLE_USER'")
	Page<User> findUsersByCreationDateWithRoleUser(@Param("startOfPeriod") Date startOfPeriod, Pageable pageable);
}