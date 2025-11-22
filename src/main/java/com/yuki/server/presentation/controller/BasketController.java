package com.yuki.server.presentation.controller;

import com.yuki.server.application.dto.AddProductRequest;
import com.yuki.server.application.dto.ApplyDiscountRequest;
import com.yuki.server.application.dto.BasketResponse;
import com.yuki.server.application.service.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 购物车控制器（表示层）.
 */
@RestController
@RequestMapping("/api/baskets")
public class BasketController {

  private final BasketService basketService;

  public BasketController(BasketService basketService) {
    this.basketService = basketService;
  }

  /**
   * 为客户创建空购物车.
   *
   * @param customerId 客户ID
   * @return 购物车响应
   */
  @PostMapping
  public ResponseEntity<BasketResponse> createBasket(@RequestParam String customerId) {
    BasketResponse basket = basketService.createBasket(customerId);
    return ResponseEntity.ok(basket);
  }

  /**
   * 获取购物车.
   *
   * @param basketId 购物车ID
   * @return 购物车响应
   */
  @GetMapping("/{basketId}")
  public ResponseEntity<BasketResponse> getBasket(@PathVariable String basketId) {
    BasketResponse basket = basketService.getBasket(basketId);
    return ResponseEntity.ok(basket);
  }

  /**
   * 根据客户ID获取购物车.
   *
   * @param customerId 客户ID
   * @return 购物车响应
   */
  @GetMapping
  public ResponseEntity<BasketResponse> getBasketByCustomerId(@RequestParam String customerId) {
    BasketResponse basket = basketService.getBasketByCustomerId(customerId);
    return ResponseEntity.ok(basket);
  }

  /**
   * 添加产品到购物车.
   *
   * @param basketId 购物车ID
   * @param request 添加产品请求
   * @return 购物车响应
   */
  @PutMapping("/{basketId}/products")
  public ResponseEntity<BasketResponse> addProduct(
      @PathVariable String basketId,
      @RequestBody AddProductRequest request) {
    BasketResponse basket = basketService.addProduct(
        basketId,
        request.getProductCode(),
        request.getQuantity());
    return ResponseEntity.ok(basket);
  }

  /**
   * 应用折扣码.
   *
   * @param basketId 购物车ID
   * @param request 应用折扣请求
   * @return 购物车响应
   */
  @PutMapping("/{basketId}/discount")
  public ResponseEntity<BasketResponse> applyDiscount(
      @PathVariable String basketId,
      @RequestBody ApplyDiscountRequest request) {
    BasketResponse basket = basketService.applyDiscount(basketId, request.getDiscountCode());
    return ResponseEntity.ok(basket);
  }
}

