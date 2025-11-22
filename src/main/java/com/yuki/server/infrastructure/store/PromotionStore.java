package com.yuki.server.infrastructure.store;

import com.yuki.server.domain.model.Promotion;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 促销数据存储（基础设施层）.
 */
@Component
public class PromotionStore {
  private final Map<String, Promotion> promotions = new ConcurrentHashMap<>();

  public void save(Promotion promotion) {
    promotions.put(promotion.getDiscountCode(), promotion);
  }

  public Promotion findByCode(String discountCode) {
    return promotions.get(discountCode);
  }

  public boolean isValid(String discountCode) {
    return promotions.containsKey(discountCode);
  }
}

