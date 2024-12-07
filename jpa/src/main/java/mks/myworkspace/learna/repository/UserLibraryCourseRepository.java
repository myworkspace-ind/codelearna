package mks.myworkspace.learna.repository;

import mks.myworkspace.learna.entity.UserLibraryCourse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface UserLibraryCourseRepository extends JpaRepository<UserLibraryCourse, Long> {

    @EntityGraph(attributePaths = {"course", "course.subcategory"})
    List<UserLibraryCourse> findByUserEid(String userEid);

    UserLibraryCourse findByUserEidAndCourseId(String userEid, Long courseId);

    @Query("SELECT SUM(c.discountedPrice) FROM UserLibraryCourse ulc " +
            "JOIN ulc.course c " +
            "WHERE ulc.paymentStatus = 'PURCHASED'")
    Double calculateTotalRevenue();


    @Query("SELECT c.id AS courseId, c.name AS courseName, COUNT(ulc.id) AS purchaseCount, SUM(c.discountedPrice) AS revenue " +
            "FROM UserLibraryCourse ulc " +
            "JOIN ulc.course c " +
            "WHERE ulc.paymentStatus = 'PURCHASED' " +
            "GROUP BY c.id, c.name")
    Page<Object[]> calculateRevenueByCourse(Pageable pageable);
	
    @Query("SELECT c.id AS courseId, c.name AS courseName, COUNT(ulc.id) AS purchaseCount, SUM(c.discountedPrice) AS revenue " +
    	       "FROM UserLibraryCourse ulc " +
    	       "JOIN ulc.course c " +
    	       "WHERE ulc.paymentStatus = 'PURCHASED' " +
    	       "AND ulc.createdDate BETWEEN :startDate AND :endDate " +  // Added filtering by createdDate
    	       "GROUP BY c.id, c.name")
    Page<Object[]> calculateRevenueByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);
    
    @Query("SELECT SUM(c.discountedPrice) " +
    	       "FROM UserLibraryCourse ulc " +
    	       "JOIN ulc.course c " +
    	       "WHERE ulc.paymentStatus = 'PURCHASED' " +
    	       "AND ulc.createdDate BETWEEN :startDate AND :endDate")
    Double calculateTotalRevenueByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT DISTINCT u.userEid FROM UserLibraryCourse u")
    List<String> findDistinctUserEids();

}








//    @Modifying
//    @Transactional
//    @Query("UPDATE UserLibraryCourse ulc SET ulc.progressStatus = 'COMPLETE' WHERE ulc.userEid = :userEid AND ulc.course.id = :courseId")
//    void updateProgressStatusToComplete(@Param("userEid") String userEid, @Param("courseId") Long courseId);
