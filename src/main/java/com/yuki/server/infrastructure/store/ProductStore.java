package com.yuki.server.infrastructure.store;

import com.yuki.server.domain.model.Product;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 产品数据存储（基础设施层）.
 */
@Component
public class ProductStore {
  private final Map<String, Product> products = new ConcurrentHashMap<>();

  public void save(Product product) {
    products.put(product.getProductCode(), product);
  }

  public Product findByCode(String productCode) {
    return products.get(productCode);
  }

  public Map<String, Product> findAll() {
    return new ConcurrentHashMap<>(products);
  }
}

