package com.yuki.server.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 错误响应DTO.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
  private String error;
  private String message;
}

