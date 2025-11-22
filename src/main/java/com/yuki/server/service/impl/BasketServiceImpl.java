package com.yuki.server.service.impl;

import com.yuki.server.dto.BasketResponse;
import com.yuki.server.exception.InvalidRequestException;
import com.yuki.server.exception.ResourceNotFoundException;
import com.yuki.server.mapper.BasketStore;
import com.yuki.server.mapper.PromotionStore;
import com.yuki.server.mapper.ProductStore;
import com.yuki.server.model.Basket;
import com.yuki.server.model.Promotion;
import com.yuki.server.service.BasketService;
import com.yuki.server.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * 购物车服务实现类.
 */
@Service
public class BasketServiceImpl implements BasketService {

  private final BasketStore basketStore;
  private final ProductStore productStore;
  private final PromotionStore promotionStore;
  private final ProductService productService;

  public BasketServiceImpl(
      BasketStore basketStore,
      ProductStore productStore,
      PromotionStore promotionStore,
      ProductService productService) {
    this.basketStore = basketStore;
    this.productStore = productStore;
    this.promotionStore = promotionStore;
    this.productService = productService;
  }

  @Override
  public BasketResponse createBasket(String customerId) {
    String basketId = UUID.randomUUID().toString();
    Basket basket = new Basket(basketId, customerId);
    basketStore.save(basket);
    return convertToResponse(basket);
  }

  @Override
  public BasketResponse getBasket(String basketId) {
    Basket basket = basketStore.findById(basketId);
    if (basket == null) {
      throw new ResourceNotFoundException("Basket not found with id: " + basketId);
    }
    return convertToResponse(basket);
  }

  @Override
  public BasketResponse getBasketByCustomerId(String customerId) {
    Basket basket = basketStore.findByCustomerId(customerId);
    if (basket == null) {
      throw new ResourceNotFoundException("Basket not found for customer: " + customerId);
    }
    return convertToResponse(basket);
  }

  @Override
  public BasketResponse addProduct(String basketId, String productCode, int quantity) {
    Basket basket = basketStore.findById(basketId);
    if (basket == null) {
      throw new ResourceNotFoundException("Basket not found with id: " + basketId);
    }

    // 验证产品是否存在
    productService.getProductByCode(productCode);

    if (quantity <= 0) {
      throw new InvalidRequestException("Quantity must be greater than 0");
    }

    basket.addProduct(productCode, quantity);
    basketStore.save(basket);
    return convertToResponse(basket);
  }

  @Override
  public BasketResponse applyDiscount(String basketId, String discountCode) {
    Basket basket = basketStore.findById(basketId);
    if (basket == null) {
      throw new ResourceNotFoundException("Basket not found with id: " + basketId);
    }

    Promotion promotion = promotionStore.findByCode(discountCode);
    if (promotion == null) {
      throw new InvalidRequestException("Invalid discount code: " + discountCode);
    }

    basket.setDiscountCode(discountCode);
    basketStore.save(basket);
    return convertToResponse(basket);
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

