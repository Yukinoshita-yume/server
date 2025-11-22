package com.yuki.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 购物车响应.
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

