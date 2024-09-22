package mks.myworkspace.learna.repository;

import mks.myworkspace.learna.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Các phương thức tìm kiếm tùy chỉnh nếu cần có thể được thêm vào đây
}
