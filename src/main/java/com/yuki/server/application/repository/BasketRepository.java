package com.yuki.server.application.repository;

import com.yuki.server.domain.model.Basket;

/**
 * 购物车仓储接口（应用层）.
 */
public interface BasketRepository {
  void save(Basket basket);

  Basket findById(String basketId);

  Basket findByCustomerId(String customerId);

  void delete(String basketId);
}

