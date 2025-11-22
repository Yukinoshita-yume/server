package com.yuki.server.service;

import com.yuki.server.dto.BasketResponse;

/**
 * 购物车服务接口.
 */
public interface BasketService {

  /**
   * 为客户创建空购物车.
   *
   * @param customerId 客户ID
   * @return 购物车响应
   */
  BasketResponse createBasket(String customerId);

  /**
   * 获取购物车.
   *
   * @param basketId 购物车ID
   * @return 购物车响应
   */
  BasketResponse getBasket(String basketId);

  /**
   * 根据客户ID获取购物车.
   *
   * @param customerId 客户ID
   * @return 购物车响应
   */
  BasketResponse getBasketByCustomerId(String customerId);

  /**
   * 添加产品到购物车.
   *
   * @param basketId 购物车ID
   * @param productCode 产品代码
   * @param quantity 数量
   * @return 购物车响应
   */
  BasketResponse addProduct(String basketId, String productCode, int quantity);

  /**
   * 应用折扣码.
   *
   * @param basketId 购物车ID
   * @param discountCode 折扣码
   * @return 购物车响应
   */
  BasketResponse applyDiscount(String basketId, String discountCode);
}
