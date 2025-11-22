package com.yuki.server.domain.exception;

/**
 * 支付异常（领域层）.
 */
public class PaymentException extends RuntimeException {
  public PaymentException(String message) {
    super(message);
  }
}

