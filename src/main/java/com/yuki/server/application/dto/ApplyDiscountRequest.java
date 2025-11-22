package com.yuki.server.application.dto;

import lombok.Data;

/**
 * 应用折扣码的请求DTO.
 */
@Data
public class ApplyDiscountRequest {
  private String discountCode;
}

