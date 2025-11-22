package com.yuki.server.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuki.server.mapper.ProductStore;
import com.yuki.server.mapper.PromotionStore;
import com.yuki.server.model.Product;
import com.yuki.server.model.Promotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 数据加载器，从data.json加载产品数据和折扣码数据.
 */
@Component
public class DataLoader implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  private final ProductStore productStore;
  private final PromotionStore promotionStore;
  private final ObjectMapper objectMapper;

  public DataLoader(ProductStore productStore, PromotionStore promotionStore, ObjectMapper objectMapper) {
    this.productStore = productStore;
    this.promotionStore = promotionStore;
    this.objectMapper = objectMapper;
  }

  @Override
  public void run(String... args) throws Exception {
    try {
      ClassPathResource resource = new ClassPathResource("static/data.json");
      InputStream inputStream = resource.getInputStream();

      Map<String, Object> data = objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});

      // 加载产品数据
      List<Map<String, Object>> productsData = (List<Map<String, Object>>) data.get("products");
      if (productsData != null) {
        for (Map<String, Object> productData : productsData) {
          String productCode = (String) productData.get("productCode");
          String name = (String) productData.get("name");
          Object priceObj = productData.get("fullPrice");
          
          BigDecimal fullPrice;
          if (priceObj instanceof Number) {
            fullPrice = BigDecimal.valueOf(((Number) priceObj).doubleValue());
          } else {
            fullPrice = new BigDecimal(priceObj.toString());
          }

          Product product = new Product(productCode, name, fullPrice);
          productStore.save(product);
          logger.info("Loaded product: {} - {}", productCode, name);
        }
        logger.info("Successfully loaded {} products", productsData.size());
      }

      // 加载折扣码数据
      List<Map<String, Object>> promotionsData = (List<Map<String, Object>>) data.get("promotions");
      if (promotionsData != null) {
        for (Map<String, Object> promotionData : promotionsData) {
          String discountCode = (String) promotionData.get("discountCode");
          Object percentObj = promotionData.get("discountPercent");
          
          BigDecimal discountPercent;
          if (percentObj instanceof Number) {
            discountPercent = BigDecimal.valueOf(((Number) percentObj).doubleValue());
          } else {
            discountPercent = new BigDecimal(percentObj.toString());
          }

          Promotion promotion = new Promotion(discountCode, discountPercent);
          promotionStore.save(promotion);
          logger.info("Loaded promotion: {} - {}%", discountCode, discountPercent);
        }
        logger.info("Successfully loaded {} promotions", promotionsData.size());
      }
    } catch (Exception e) {
      logger.error("Error loading data from data.json", e);
      throw e;
    }
  }
}

