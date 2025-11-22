package com.yuki.server.infrastructure.exception;

import com.yuki.server.application.dto.ErrorResponse;
import com.yuki.server.domain.exception.InvalidRequestException;
import com.yuki.server.domain.exception.PaymentException;
import com.yuki.server.domain.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器（基础设施层）.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
    logger.error("Resource not found: {}", ex.getMessage());
    ErrorResponse error = new ErrorResponse("RESOURCE_NOT_FOUND", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex) {
    logger.error("Invalid request: {}", ex.getMessage());
    ErrorResponse error = new ErrorResponse("INVALID_REQUEST", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(PaymentException.class)
  public ResponseEntity<ErrorResponse> handlePaymentException(PaymentException ex) {
    logger.error("Payment error: {}", ex.getMessage());
    ErrorResponse error = new ErrorResponse("PAYMENT_ERROR", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    logger.error("Unexpected error: ", ex);
    ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}

