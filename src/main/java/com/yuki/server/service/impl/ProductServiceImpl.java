package com.yuki.server.service.impl;

import com.yuki.server.dto.ProductResponse;
import com.yuki.server.exception.ResourceNotFoundException;
import com.yuki.server.mapper.ProductStore;
import com.yuki.server.model.Product;
import com.yuki.server.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 产品服务实现类.
 */
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductStore productStore;

  public ProductServiceImpl(ProductStore productStore) {
    this.productStore = productStore;
  }

  @Override
  public List<ProductResponse> getAllProducts() {
    Map<String, Product> products = productStore.findAll();
    return products.values().stream()
        .map(product -> new ProductResponse(
            product.getProductCode(),
            product.getName(),
            product.getFullPrice()))
        .collect(Collectors.toList());
  }

  @Override
  public Product getProductByCode(String productCode) {
    Product product = productStore.findByCode(productCode);
    if (product == null) {
      throw new ResourceNotFoundException(
          "Product not found with code: " + productCode);
    }
    return product;
  }

  @Override
  public Map<String, Product> getAllProductsMap() {
    return productStore.findAll();
  }
}

