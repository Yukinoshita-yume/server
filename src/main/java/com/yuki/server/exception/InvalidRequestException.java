package com.yuki.server.exception;

/**
 * 无效请求异常.
 */
public class InvalidRequestException extends RuntimeException {
  public InvalidRequestException(String message) {
    super(message);
  }
}

