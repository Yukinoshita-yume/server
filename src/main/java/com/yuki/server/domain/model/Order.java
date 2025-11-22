package com.yuki.server.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单领域模型.
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

