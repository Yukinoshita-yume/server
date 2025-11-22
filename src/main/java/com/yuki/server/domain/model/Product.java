package com.yuki.server.domain.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 产品领域模型.
 */
@Data
public class Product {
  private final String productCode;
  private final String name;
  private final BigDecimal fullPrice;
}

