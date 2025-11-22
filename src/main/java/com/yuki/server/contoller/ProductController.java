package com.yuki.server.contoller;

import com.yuki.server.dto.ProductResponse;
import com.yuki.server.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 产品控制器.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  /**
   * 获取所有产品列表.
   *
   * @return 产品列表
   */
  @GetMapping
  public ResponseEntity<List<ProductResponse>> getAllProducts() {
    List<ProductResponse> products = productService.getAllProducts();
    return ResponseEntity.ok(products);
  }
}

