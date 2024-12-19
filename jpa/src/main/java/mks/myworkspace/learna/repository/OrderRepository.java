package mks.myworkspace.learna.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import mks.myworkspace.learna.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderCode(String orderCode);
    List<Order> findByUserEid(String userEid);
}
