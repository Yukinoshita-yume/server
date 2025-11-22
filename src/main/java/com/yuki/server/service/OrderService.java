package com.yuki.server.service;

import com.yuki.server.dto.OrderResponse;

import java.util.List;

/**
 * 订单服务接口.
 */
public interface OrderService {

  /**
   * 将购物车转换为订单（结账）.
   *
   * @param basketId 购物车ID
   * @param cardNumber 信用卡号
   * @param expiryDate 过期日期
   * @return 订单响应
   */
  OrderResponse checkout(String basketId, String cardNumber, String expiryDate);

  /**
   * 获取所有订单.
   *
   * @return 订单列表
   */
  List<OrderResponse> getAllOrders();

  /**
   * 根据客户ID获取订单.
   *
   * @param customerId 客户ID
   * @return 订单列表
   */
  List<OrderResponse> getOrdersByCustomerId(String customerId);
}
