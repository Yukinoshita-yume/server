package com.yuki.server.application.dto;

import lombok.Data;

/**
 * 结账请求DTO.
 */
@Data
public class CheckoutRequest {
  private String cardNumber;
  private String expiryDate; // 格式: MM/yy
}

