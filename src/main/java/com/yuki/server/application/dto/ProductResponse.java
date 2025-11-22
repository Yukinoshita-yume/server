package com.yuki.server.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 产品响应DTO.
 */
@Data
@AllArgsConstructor
public class ProductResponse {
  private String productCode;
  private String name;
  private BigDecimal fullPrice;
}

