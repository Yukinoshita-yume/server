package com.yuki.server.infrastructure.store;

import com.yuki.server.domain.model.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单数据存储（基础设施层）.
 */
@Component
public class OrderStore {
  private final Map<String, Order> orders = new ConcurrentHashMap<>();

  public void save(Order order) {
    orders.put(order.getOrderId(), order);
  }

  public Order findById(String orderId) {
    return orders.get(orderId);
  }

  public List<Order> findAll() {
    return new ArrayList<>(orders.values());
  }

  public List<Order> findByCustomerId(String customerId) {
    return orders.values().stream()
        .filter(order -> order.getCustomerId().equals(customerId))
        .toList();
  }
}

