package com.yuki.server.exception;

/**
 * 支付异常.
 */
public class PaymentException extends RuntimeException {
  public PaymentException(String message) {
    super(message);
  }
}

