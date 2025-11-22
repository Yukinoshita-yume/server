package com.yuki.server.service.impl;

import com.yuki.server.exception.PaymentException;
import com.yuki.server.service.CreditCardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 信用卡服务实现类，用于验证信用卡号和过期日期.
 */
@Service
public class CreditCardServiceImpl implements CreditCardService {

  @Override
  public boolean isValidCardNumber(String cardNumber) {
    if (cardNumber == null || cardNumber.trim().isEmpty()) {
      return false;
    }

    // 移除所有非数字字符
    String digitsOnly = cardNumber.replaceAll("[^0-9]", "");
    
    if (digitsOnly.length() < 13 || digitsOnly.length() > 19) {
      return false;
    }

    return luhnCheck(digitsOnly);
  }

  /**
   * Luhn算法验证.
   *
   * @param cardNumber 信用卡号（仅数字）
   * @return 是否通过Luhn验证
   */
  private boolean luhnCheck(String cardNumber) {
    int sum = 0;
    boolean alternate = false;

    // 从右到左处理每个数字
    for (int i = cardNumber.length() - 1; i >= 0; i--) {
      int digit = Character.getNumericValue(cardNumber.charAt(i));

      if (alternate) {
        digit *= 2;
        if (digit > 9) {
          digit = (digit % 10) + 1;
        }
      }

      sum += digit;
      alternate = !alternate;
    }

    return (sum % 10) == 0;
  }

  @Override
  public boolean isValidExpiryDate(String expiryDate) {
    if (expiryDate == null || expiryDate.trim().isEmpty()) {
      return false;
    }

    try {
      // 解析MM/yy格式
      String[] parts = expiryDate.split("/");
      if (parts.length != 2) {
        return false;
      }
      
      int month = Integer.parseInt(parts[0]);
      int year = Integer.parseInt(parts[1]);
      
      // 将yy转换为yyyy（假设20xx）
      int fullYear = 2000 + year;
      
      // 创建该月的第一天
      LocalDate expiry = LocalDate.of(fullYear, month, 1);
      // 设置为该月的最后一天
      expiry = expiry.withDayOfMonth(expiry.lengthOfMonth());
      
      LocalDate today = LocalDate.now();
      return expiry.isAfter(today) || expiry.isEqual(today);
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void processPayment(String cardNumber, String expiryDate) {
    // 验证信用卡号
    if (!isValidCardNumber(cardNumber)) {
      throw new PaymentException("Invalid card number. Card number failed Luhn algorithm validation.");
    }

    // 验证过期日期
    if (!isValidExpiryDate(expiryDate)) {
      throw new PaymentException("Invalid or expired card. Expiry date must be later than today.");
    }

    // 模拟支付处理（这里只是验证，实际支付已通过验证）
  }
}

