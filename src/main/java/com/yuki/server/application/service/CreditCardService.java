package com.yuki.server.application.service;

import com.yuki.server.domain.exception.PaymentException;

/**
 * 信用卡服务接口（应用层）.
 */
public interface CreditCardService {

  /**
   * 验证信用卡号是否有效（使用Luhn算法）.
   *
   * @param cardNumber 信用卡号
   * @return 是否有效
   */
  boolean isValidCardNumber(String cardNumber);

  /**
   * 验证过期日期是否有效且未过期.
   *
   * @param expiryDate 过期日期，格式为 MM/yy
   * @return 是否有效且未过期
   */
  boolean isValidExpiryDate(String expiryDate);

  /**
   * 处理支付.
   *
   * @param cardNumber 信用卡号
   * @param expiryDate 过期日期
   * @throws PaymentException 如果支付失败
   */
  void processPayment(String cardNumber, String expiryDate);
}

