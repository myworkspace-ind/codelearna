package mks.myworkspace.learna.repository;

import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Order;

@Repository
public class OrderJdbcRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderRepository orderRepository;
	
    public Order save(Order order) {
        if (orderRepository.findByOrderCode(order.getOrderCode()).isEmpty()) {
            String sql = "INSERT INTO learna_orders (order_code, user_eid, course_id, payment_method, amount, status, created_at) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int paramIndex = 1;
                ps.setString(paramIndex++, order.getOrderCode().toString());
                ps.setString(paramIndex++, order.getUserEid().toString());
                ps.setLong(paramIndex++, order.getCourseId());
                ps.setString(paramIndex++, order.getPaymentMethod().toString());
                ps.setBigDecimal(paramIndex++, order.getAmount());
                ps.setString(paramIndex++, "PENDING");
                ps.setTimestamp(paramIndex++, Timestamp.valueOf(LocalDateTime.now()));
                return ps;
            }, keyHolder);

        } else {
            String sql = "UPDATE learna_orders SET user_eid = ?, course_id = ?, payment_method = ?, amount = ?, status = ? "
                       + "WHERE order_code = ?";

            int rowsAffected = jdbcTemplate.update(sql,
                    order.getUserEid(),
                    order.getCourseId(),
                    order.getPaymentMethod(),
                    order.getAmount(),
                    order.getStatus().name(),
                    order.getOrderCode()
            );

            if (rowsAffected == 0) {
                throw new RuntimeException("Order not found with order code: " + order.getOrderCode());
            }
        }

        return order;
    }
}
