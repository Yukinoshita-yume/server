package com.yuki.server.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 持有用户想要购买的0..*个产品.
 */
@Data
public class Basket {
  private final String basketId;
  private final String customerId;
  private final Map<String, Integer> products; // productCode -> quantity
  private String discountCode;

  public Basket(String basketId, String customerId) {
    this.basketId = basketId;
    this.customerId = customerId;
    this.products = new HashMap<>();
    this.discountCode = null;
  }

  /**
   * 添加产品到购物车.
   *
   * @param productCode 产品代码
   * @param quantity 数量
   */
  public void addProduct(String productCode, int quantity) {
    products.put(productCode, products.getOrDefault(productCode, 0) + quantity);
  }

  /**
   * 计算总价（不含折扣）.
   *
   * @param productMap 产品映射
   * @return 总价
   */
  public BigDecimal calculateTotalPrice(Map<String, Product> productMap) {
    BigDecimal total = BigDecimal.ZERO;
    for (Map.Entry<String, Integer> entry : products.entrySet()) {
      Product product = productMap.get(entry.getKey());
      if (product != null) {
        total = total.add(product.getFullPrice().multiply(BigDecimal.valueOf(entry.getValue())));
      }
    }
    return total;
  }
}

