package com.yuki.server.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单响应DTO.
 */
@Data
@AllArgsConstructor
public class OrderResponse {
  private String orderId;
  private String customerId;
  private BigDecimal totalPrice;
  private LocalDateTime createdAt;
  private String discountCode;
}

