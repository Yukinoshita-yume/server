package com.yuki.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 表示想要购买产品的客户.
 */
@Data
@AllArgsConstructor
public class Customer {
  private final String customerId;
}

