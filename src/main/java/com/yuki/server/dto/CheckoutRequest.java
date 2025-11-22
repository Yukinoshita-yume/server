package com.yuki.server.dto;

import lombok.Data;

/**
 * 结账请求.
 */
@Data
public class CheckoutRequest {
  private String cardNumber;
  private String expiryDate; // 格式: MM/yy
}

