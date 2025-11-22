package com.yuki.server.infrastructure.repository;

import com.yuki.server.application.repository.BasketRepository;
import com.yuki.server.domain.model.Basket;
import com.yuki.server.infrastructure.store.BasketStore;
import org.springframework.stereotype.Repository;

/**
 * 购物车仓储实现（基础设施层）.
 */
@Repository
public class BasketRepositoryImpl implements BasketRepository {

  private final BasketStore basketStore;

  public BasketRepositoryImpl(BasketStore basketStore) {
    this.basketStore = basketStore;
  }

  @Override
  public void save(Basket basket) {
    basketStore.save(basket);
  }

  @Override
  public Basket findById(String basketId) {
    return basketStore.findById(basketId);
  }

  @Override
  public Basket findByCustomerId(String customerId) {
    return basketStore.findByCustomerId(customerId);
  }

  @Override
  public void delete(String basketId) {
    basketStore.delete(basketId);
  }
}

