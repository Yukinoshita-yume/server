package com.yuki.server.application.service.impl;

import com.yuki.server.application.dto.ProductResponse;
import com.yuki.server.application.repository.ProductRepository;
import com.yuki.server.application.service.ProductService;
import com.yuki.server.domain.exception.ResourceNotFoundException;
import com.yuki.server.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 产品服务实现类（应用层）.
 */
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public List<ProductResponse> getAllProducts() {
    Map<String, Product> products = productRepository.findAll();
    return products.values().stream()
        .map(product -> new ProductResponse(
            product.getProductCode(),
            product.getName(),
            product.getFullPrice()))
        .collect(Collectors.toList());
  }

  @Override
  public Product getProductByCode(String productCode) {
    Product product = productRepository.findByCode(productCode);
    if (product == null) {
      throw new ResourceNotFoundException(
          "Product not found with code: " + productCode);
    }
    return product;
  }

  @Override
  public Map<String, Product> getAllProductsMap() {
    return productRepository.findAll();
  }
}

