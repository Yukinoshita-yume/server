package com.yuki.server.application.service.impl;

import com.yuki.server.application.dto.OrderResponse;
import com.yuki.server.application.repository.BasketRepository;
import com.yuki.server.application.repository.OrderRepository;
import com.yuki.server.application.repository.ProductRepository;
import com.yuki.server.application.repository.PromotionRepository;
import com.yuki.server.application.service.CreditCardService;
import com.yuki.server.application.service.OrderService;
import com.yuki.server.domain.exception.ResourceNotFoundException;
import com.yuki.server.domain.model.Basket;
import com.yuki.server.domain.model.Order;
import com.yuki.server.domain.model.Promotion;
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
 * 订单服务实现类（应用层）.
 */
@Service
public class OrderServiceImpl implements OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

  private final OrderRepository orderRepository;
  private final BasketRepository basketRepository;
  private final ProductRepository productRepository;
  private final PromotionRepository promotionRepository;
  private final CreditCardService creditCardService;

  public OrderServiceImpl(
      OrderRepository orderRepository,
      BasketRepository basketRepository,
      ProductRepository productRepository,
      PromotionRepository promotionRepository,
      CreditCardService creditCardService) {
    this.orderRepository = orderRepository;
    this.basketRepository = basketRepository;
    this.productRepository = productRepository;
    this.promotionRepository = promotionRepository;
    this.creditCardService = creditCardService;
  }

  @Override
  public OrderResponse checkout(String basketId, String cardNumber, String expiryDate) {
    Basket basket = basketRepository.findById(basketId);
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

    orderRepository.save(order);

    // 删除购物车
    basketRepository.delete(basketId);

    // 记录日志
    logger.info("Successfully converted basket {} to order {} for customer {} with total price {}",
        basketId, orderId, basket.getCustomerId(), totalPrice);

    return convertToResponse(order);
  }

  @Override
  public List<OrderResponse> getAllOrders() {
    return orderRepository.findAll().stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderResponse> getOrdersByCustomerId(String customerId) {
    return orderRepository.findByCustomerId(customerId).stream()
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
    Map<String, com.yuki.server.domain.model.Product> productMap = productRepository.findAll();
    BigDecimal total = basket.calculateTotalPrice(productMap);

    if (basket.getDiscountCode() != null) {
      Promotion promotion = promotionRepository.findByCode(basket.getDiscountCode());
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

