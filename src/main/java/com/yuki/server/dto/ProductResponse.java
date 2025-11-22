package com.yuki.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 产品响应.
 */
@Data
@AllArgsConstructor
public class ProductResponse {
  private String productCode;
  private String name;
  private BigDecimal fullPrice;
}

