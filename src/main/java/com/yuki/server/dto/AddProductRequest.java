package com.yuki.server.dto;

import lombok.Data;

/**
 * 添加产品到购物车的请求.
 */
@Data
public class AddProductRequest {
  private String productCode;
  private Integer quantity;
}

