package com.yuki.server.application.repository;

import com.yuki.server.domain.model.Order;

import java.util.List;

/**
 * 订单仓储接口（应用层）.
 */
public interface OrderRepository {
  void save(Order order);

  Order findById(String orderId);

  List<Order> findAll();

  List<Order> findByCustomerId(String customerId);
}

