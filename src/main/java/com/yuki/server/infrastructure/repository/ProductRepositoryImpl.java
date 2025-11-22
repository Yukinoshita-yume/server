package com.yuki.server.infrastructure.repository;

import com.yuki.server.application.repository.ProductRepository;
import com.yuki.server.domain.model.Product;
import com.yuki.server.infrastructure.store.ProductStore;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 产品仓储实现（基础设施层）.
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private final ProductStore productStore;

  public ProductRepositoryImpl(ProductStore productStore) {
    this.productStore = productStore;
  }

  @Override
  public void save(Product product) {
    productStore.save(product);
  }

  @Override
  public Product findByCode(String productCode) {
    return productStore.findByCode(productCode);
  }

  @Override
  public Map<String, Product> findAll() {
    return productStore.findAll();
  }
}

