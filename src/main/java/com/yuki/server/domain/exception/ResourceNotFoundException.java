package com.yuki.server.domain.exception;

/**
 * 资源未找到异常（领域层）.
 */
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}

