package com.yuki.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 当使用支付购买Basket时保存的对象.
 * 订单应包含总价。如果支付时提供了有效的折扣码，总价应相应减少.
 */
@Data
@AllArgsConstructor
public class Order {
  private final String orderId;
  private final String customerId;
  private final BigDecimal totalPrice;
  private final LocalDateTime createdAt;
  private final String discountCode;
}

