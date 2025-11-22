package com.yuki.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 错误响应.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
  private String error;
  private String message;
}

