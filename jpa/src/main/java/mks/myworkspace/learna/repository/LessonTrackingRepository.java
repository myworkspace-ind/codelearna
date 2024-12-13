package mks.myworkspace.learna.repository;

import mks.myworkspace.learna.entity.LessonTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonTrackingRepository extends JpaRepository<LessonTracking, Long> {

    Optional<LessonTracking> findByUserEidAndCourseIdAndLessonId(String userEid, Long courseId, Long lessonId);
    @Query("SELECT COUNT(lt) FROM LessonTracking lt WHERE lt.courseId = :courseId")
    Long countLessonsByCourseId(Long courseId);

    @Query("SELECT COUNT(lt) FROM LessonTracking lt WHERE lt.userEid = :userEid AND lt.courseId = :courseId")
    Long countByUserEidAndCourseId(String userEid, Long courseId);
    
    @Query("SELECT COUNT(lt) FROM LessonTracking lt WHERE lt.userEid = :userEid AND lt.courseId = :courseId")
    Long countCompletedLessonsByUserAndCourse(@Param("userEid") String userEid, @Param("courseId") Long courseId);


}
