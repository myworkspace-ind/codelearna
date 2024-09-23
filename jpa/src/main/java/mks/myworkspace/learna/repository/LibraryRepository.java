package mks.myworkspace.learna.repository;

import mks.myworkspace.learna.entity.Library;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {

    @EntityGraph(attributePaths = {"user", "course"})
    List<Library> findByUserId(Long userId);
}