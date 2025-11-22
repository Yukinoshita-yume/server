package com.yuki.server.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 购物车响应DTO.
 */
@Data
@AllArgsConstructor
public class BasketResponse {
  private String basketId;
  private String customerId;
  private Map<String, Integer> products;
  private String discountCode;
  private BigDecimal totalPrice;
}

