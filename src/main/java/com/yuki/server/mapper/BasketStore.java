package com.yuki.server.mapper;

import com.yuki.server.model.Basket;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 购物车数据存储.
 */
@Component
public class BasketStore {
  private final Map<String, Basket> baskets = new ConcurrentHashMap<>();

  public void save(Basket basket) {
    baskets.put(basket.getBasketId(), basket);
  }

  public Basket findById(String basketId) {
    return baskets.get(basketId);
  }

  public Basket findByCustomerId(String customerId) {
    return baskets.values().stream()
        .filter(basket -> basket.getCustomerId().equals(customerId))
        .findFirst()
        .orElse(null);
  }

  public void delete(String basketId) {
    baskets.remove(basketId);
  }
}

