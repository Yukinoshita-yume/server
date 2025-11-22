package com.yuki.server.infrastructure.repository;

import com.yuki.server.application.repository.PromotionRepository;
import com.yuki.server.domain.model.Promotion;
import com.yuki.server.infrastructure.store.PromotionStore;
import org.springframework.stereotype.Repository;

/**
 * 促销仓储实现（基础设施层）.
 */
@Repository
public class PromotionRepositoryImpl implements PromotionRepository {

  private final PromotionStore promotionStore;

  public PromotionRepositoryImpl(PromotionStore promotionStore) {
    this.promotionStore = promotionStore;
  }

  @Override
  public void save(Promotion promotion) {
    promotionStore.save(promotion);
  }

  @Override
  public Promotion findByCode(String discountCode) {
    return promotionStore.findByCode(discountCode);
  }

  @Override
  public boolean isValid(String discountCode) {
    return promotionStore.isValid(discountCode);
  }
}

