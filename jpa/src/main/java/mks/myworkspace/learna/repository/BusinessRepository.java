package mks.myworkspace.learna.repository;

import mks.myworkspace.learna.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    // Thêm các phương thức truy vấn dữ liệu của Business tại đây
}