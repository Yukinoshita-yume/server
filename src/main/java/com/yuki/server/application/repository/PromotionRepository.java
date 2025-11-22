package com.yuki.server.application.repository;

import com.yuki.server.domain.model.Promotion;

/**
 * 促销仓储接口（应用层）.
 */
public interface PromotionRepository {
  void save(Promotion promotion);

  Promotion findByCode(String discountCode);

  boolean isValid(String discountCode);
}

