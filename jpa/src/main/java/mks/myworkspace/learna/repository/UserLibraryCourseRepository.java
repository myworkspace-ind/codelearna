package mks.myworkspace.learna.repository;

import mks.myworkspace.learna.entity.UserLibraryCourse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface UserLibraryCourseRepository extends JpaRepository<UserLibraryCourse, Long> {

    @EntityGraph(attributePaths = {"course", "course.subcategory"})
    List<UserLibraryCourse> findByUserEid(String userEid);

    UserLibraryCourse findByUserEidAndCourseId(String userEid, Long courseId);
    
//    @Modifying
//    @Transactional
//    @Query("UPDATE UserLibraryCourse ulc SET ulc.progressStatus = 'COMPLETE' WHERE ulc.userEid = :userEid AND ulc.course.id = :courseId")
//    void updateProgressStatusToComplete(@Param("userEid") String userEid, @Param("courseId") Long courseId);



}