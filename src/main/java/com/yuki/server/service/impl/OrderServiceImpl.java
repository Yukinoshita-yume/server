package com.yuki.server.service.impl;

import com.yuki.server.dto.OrderResponse;
import com.yuki.server.exception.ResourceNotFoundException;
import com.yuki.server.mapper.BasketStore;
import com.yuki.server.mapper.OrderStore;
import com.yuki.server.mapper.ProductStore;
import com.yuki.server.mapper.PromotionStore;
import com.yuki.server.model.Basket;
import com.yuki.server.model.Order;
import com.yuki.server.model.Promotion;
import com.yuki.server.service.CreditCardService;
import com.yuki.server.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 订单服务实现类.
 */
@Service
public class OrderServiceImpl implements OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

  private final OrderStore orderStore;
  private final BasketStore basketStore;
  private final ProductStore productStore;
  private final PromotionStore promotionStore;
  private final CreditCardService creditCardService;

  public OrderServiceImpl(
      OrderStore orderStore,
      BasketStore basketStore,
      ProductStore productStore,
      PromotionStore promotionStore,
      CreditCardService creditCardService) {
    this.orderStore = orderStore;
    this.basketStore = basketStore;
    this.productStore = productStore;
    this.promotionStore = promotionStore;
    this.creditCardService = creditCardService;
  }

  @Override
  public OrderResponse checkout(String basketId, String cardNumber, String expiryDate) {
    Basket basket = basketStore.findById(basketId);
    if (basket == null) {
      throw new ResourceNotFoundException("Basket not found with id: " + basketId);
    }

    // 验证支付
    creditCardService.processPayment(cardNumber, expiryDate);

    // 计算总价（含折扣）
    BigDecimal totalPrice = calculateTotalPriceWithDiscount(basket);

    // 创建订单
    String orderId = UUID.randomUUID().toString();
    Order order = new Order(
        orderId,
        basket.getCustomerId(),
        totalPrice,
        LocalDateTime.now(),
        basket.getDiscountCode());

    orderStore.save(order);

    // 删除购物车
    basketStore.delete(basketId);

    // 记录日志
    logger.info("Successfully converted basket {} to order {} for customer {} with total price {}",
        basketId, orderId, basket.getCustomerId(), totalPrice);

    return convertToResponse(order);
  }

  @Override
  public List<OrderResponse> getAllOrders() {
    return orderStore.findAll().stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderResponse> getOrdersByCustomerId(String customerId) {
    return orderStore.findByCustomerId(customerId).stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  /**
   * 计算总价（含折扣）.
   *
   * @param basket 购物车
   * @return 总价
   */
  private BigDecimal calculateTotalPriceWithDiscount(Basket basket) {
    Map<String, com.yuki.server.model.Product> productMap = productStore.findAll();
    BigDecimal total = basket.calculateTotalPrice(productMap);

    if (basket.getDiscountCode() != null) {
      Promotion promotion = promotionStore.findByCode(basket.getDiscountCode());
      if (promotion != null) {
        BigDecimal discountAmount = total.multiply(promotion.getDiscountPercent())
            .divide(new BigDecimal("100"));
        total = total.subtract(discountAmount);
      }
    }

    return total;
  }

  /**
   * 转换为响应DTO.
   *
   * @param order 订单
   * @return 订单响应
   */
  private OrderResponse convertToResponse(Order order) {
    return new OrderResponse(
        order.getOrderId(),
        order.getCustomerId(),
        order.getTotalPrice(),
        order.getCreatedAt(),
        order.getDiscountCode());
  }
}

