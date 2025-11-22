package com.yuki.server.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 促销领域模型.
 */
@Data
@AllArgsConstructor
public class Promotion {
  private final String discountCode;
  private final BigDecimal discountPercent;
}

