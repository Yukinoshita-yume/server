package com.yuki.server.application.service.impl;

import com.yuki.server.application.dto.BasketResponse;
import com.yuki.server.application.repository.BasketRepository;
import com.yuki.server.application.repository.ProductRepository;
import com.yuki.server.application.repository.PromotionRepository;
import com.yuki.server.application.service.BasketService;
import com.yuki.server.application.service.ProductService;
import com.yuki.server.domain.exception.InvalidRequestException;
import com.yuki.server.domain.exception.ResourceNotFoundException;
import com.yuki.server.domain.model.Basket;
import com.yuki.server.domain.model.Promotion;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * 购物车服务实现类（应用层）.
 */
@Service
public class BasketServiceImpl implements BasketService {

  private final BasketRepository basketRepository;
  private final ProductRepository productRepository;
  private final PromotionRepository promotionRepository;
  private final ProductService productService;

  public BasketServiceImpl(
      BasketRepository basketRepository,
      ProductRepository productRepository,
      PromotionRepository promotionRepository,
      ProductService productService) {
    this.basketRepository = basketRepository;
    this.productRepository = productRepository;
    this.promotionRepository = promotionRepository;
    this.productService = productService;
  }

  @Override
  public BasketResponse createBasket(String customerId) {
    String basketId = UUID.randomUUID().toString();
    Basket basket = new Basket(basketId, customerId);
    basketRepository.save(basket);
    return convertToResponse(basket);
  }

  @Override
  public BasketResponse getBasket(String basketId) {
    Basket basket = basketRepository.findById(basketId);
    if (basket == null) {
      throw new ResourceNotFoundException("Basket not found with id: " + basketId);
    }
    return convertToResponse(basket);
  }

  @Override
  public BasketResponse getBasketByCustomerId(String customerId) {
    Basket basket = basketRepository.findByCustomerId(customerId);
    if (basket == null) {
      throw new ResourceNotFoundException("Basket not found for customer: " + customerId);
    }
    return convertToResponse(basket);
  }

  @Override
  public BasketResponse addProduct(String basketId, String productCode, int quantity) {
    Basket basket = basketRepository.findById(basketId);
    if (basket == null) {
      throw new ResourceNotFoundException("Basket not found with id: " + basketId);
    }

    // 验证产品是否存在
    productService.getProductByCode(productCode);

    if (quantity <= 0) {
      throw new InvalidRequestException("Quantity must be greater than 0");
    }

    basket.addProduct(productCode, quantity);
    basketRepository.save(basket);
    return convertToResponse(basket);
  }

  @Override
  public BasketResponse applyDiscount(String basketId, String discountCode) {
    Basket basket = basketRepository.findById(basketId);
    if (basket == null) {
      throw new ResourceNotFoundException("Basket not found with id: " + basketId);
    }

    Promotion promotion = promotionRepository.findByCode(discountCode);
    if (promotion == null) {
      throw new InvalidRequestException("Invalid discount code: " + discountCode);
    }

    basket.setDiscountCode(discountCode);
    basketRepository.save(basket);
    return convertToResponse(basket);
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
   * @param basket 购物车
   * @return 购物车响应
   */
  private BasketResponse convertToResponse(Basket basket) {
    return new BasketResponse(
        basket.getBasketId(),
        basket.getCustomerId(),
        basket.getProducts(),
        basket.getDiscountCode(),
        calculateTotalPriceWithDiscount(basket));
  }
}

