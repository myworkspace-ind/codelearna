package mks.myworkspace.learna.repository;

import mks.myworkspace.learna.entity.UserLibraryCourse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLibraryCourseRepository extends JpaRepository<UserLibraryCourse, Long> {

    @EntityGraph(attributePaths = {"course", "course.subcategory"})
    List<UserLibraryCourse> findByUserEid(String userEid);

    UserLibraryCourse findByUserEidAndCourseId(String userEid, Long courseId);
}