package com.yuki.server.infrastructure.repository;

import com.yuki.server.application.repository.OrderRepository;
import com.yuki.server.domain.model.Order;
import com.yuki.server.infrastructure.store.OrderStore;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单仓储实现（基础设施层）.
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

  private final OrderStore orderStore;

  public OrderRepositoryImpl(OrderStore orderStore) {
    this.orderStore = orderStore;
  }

  @Override
  public void save(Order order) {
    orderStore.save(order);
  }

  @Override
  public Order findById(String orderId) {
    return orderStore.findById(orderId);
  }

  @Override
  public List<Order> findAll() {
    return orderStore.findAll();
  }

  @Override
  public List<Order> findByCustomerId(String customerId) {
    return orderStore.findByCustomerId(customerId);
  }
}

