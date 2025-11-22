package com.yuki.server.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 客户领域模型.
 */
@Data
@AllArgsConstructor
public class Customer {
  private final String customerId;
}

