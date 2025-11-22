package com.yuki.server.dto;

import lombok.Data;

/**
 * 应用折扣码的请求.
 */
@Data
public class ApplyDiscountRequest {
  private String discountCode;
}

