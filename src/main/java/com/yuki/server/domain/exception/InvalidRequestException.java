package com.yuki.server.domain.exception;

/**
 * 无效请求异常（领域层）.
 */
public class InvalidRequestException extends RuntimeException {
  public InvalidRequestException(String message) {
    super(message);
  }
}

