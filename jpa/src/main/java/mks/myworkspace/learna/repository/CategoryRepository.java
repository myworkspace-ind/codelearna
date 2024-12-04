package mks.myworkspace.learna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mks.myworkspace.learna.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}