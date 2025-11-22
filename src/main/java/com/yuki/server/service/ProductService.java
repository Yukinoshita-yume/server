package com.yuki.server.service;

import com.yuki.server.dto.ProductResponse;
import com.yuki.server.model.Product;

import java.util.List;
import java.util.Map;

/**
 * 产品服务接口.
 */
public interface ProductService {

  /**
   * 获取所有产品.
   *
   * @return 产品列表
   */
  List<ProductResponse> getAllProducts();

  /**
   * 根据产品代码获取产品.
   *
   * @param productCode 产品代码
   * @return 产品
   */
  Product getProductByCode(String productCode);

  /**
   * 获取所有产品映射.
   *
   * @return 产品映射
   */
  Map<String, Product> getAllProductsMap();
}
