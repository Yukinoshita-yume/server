package com.yuki.server.contoller;

import com.yuki.server.dto.CheckoutRequest;
import com.yuki.server.dto.OrderResponse;
import com.yuki.server.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单控制器.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  /**
   * 将购物车转换为订单（结账）.
   *
   * @param basketId 购物车ID
   * @param request 结账请求
   * @return 订单响应
   */
  @PostMapping("/checkout/{basketId}")
  public ResponseEntity<OrderResponse> checkout(
      @PathVariable String basketId,
      @RequestBody CheckoutRequest request) {
    OrderResponse order = orderService.checkout(
        basketId,
        request.getCardNumber(),
        request.getExpiryDate());
    return ResponseEntity.ok(order);
  }

  /**
   * 获取所有订单列表.
   *
   * @return 订单列表
   */
  @GetMapping
  public ResponseEntity<List<OrderResponse>> getAllOrders() {
    List<OrderResponse> orders = orderService.getAllOrders();
    return ResponseEntity.ok(orders);
  }

  /**
   * 根据客户ID获取订单列表.
   *
   * @param customerId 客户ID
   * @return 订单列表
   */
  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<OrderResponse>> getOrdersByCustomerId(
      @PathVariable String customerId) {
    List<OrderResponse> orders = orderService.getOrdersByCustomerId(customerId);
    return ResponseEntity.ok(orders);
  }
}

