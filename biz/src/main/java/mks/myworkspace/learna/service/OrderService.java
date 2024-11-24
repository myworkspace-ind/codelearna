package mks.myworkspace.learna.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import mks.myworkspace.learna.entity.Order;
import mks.myworkspace.learna.repository.OrderRepository;

public interface OrderService {
	public String generateQrCodeUrl(String orderCode, BigDecimal amount);
	public String generateOrderCode();
	public Order createOrder(String paymentMethod, String userEid, Long courseId);
	public Optional<Order> getOrder(String orderCode);
	public boolean updateOrderStatus(String orderCode, Order.OrderStatus status);
}