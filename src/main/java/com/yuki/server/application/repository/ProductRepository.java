package com.yuki.server.application.repository;

import com.yuki.server.domain.model.Product;

import java.util.Map;

/**
 * 产品仓储接口（应用层）.
 */
public interface ProductRepository {
  void save(Product product);

  Product findByCode(String productCode);

  Map<String, Product> findAll();
}

