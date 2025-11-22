package com.yuki.server.application.dto;

import lombok.Data;

/**
 * 添加产品到购物车的请求DTO.
 */
@Data
public class AddProductRequest {
  private String productCode;
  private Integer quantity;
}

