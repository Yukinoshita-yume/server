package com.yuki.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 表示折扣码和折扣百分比.
 */
@Data
@AllArgsConstructor
public class Promotion {
  private final String discountCode;
  private final BigDecimal discountPercent;
}

