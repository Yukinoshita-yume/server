# 电商购物车系统

这是一个基于Spring Boot的电商购物车Web服务系统，采用Clean Architecture（清洁架构）设计，实现了产品管理、购物车管理、订单处理和支付验证等功能。

## 项目概述

本项目是一个完整的电商购物车系统后端服务，采用Clean Architecture分层架构设计，遵循Google Java编码规范，使用Java 17和Spring Boot 3.5.8框架开发。

## 架构设计

本项目采用 **Clean Architecture(清洁架构)** 原则，将代码分为四个主要层次：

1. **Presentation Layer（表示层）**: 处理HTTP请求和响应
2. **Application Layer（应用层）**: 包含业务逻辑、服务接口和实现、Repository接口、DTO
3. **Domain Layer（领域层）**: 包含领域模型和领域异常
4. **Infrastructure Layer（基础设施层）**: 包含Repository实现、数据存储、配置、异常处理

### 依赖方向

```
Presentation → Application → Domain
Infrastructure → Application (通过Repository接口)
```

- **应用层不依赖基础设施层**：通过Repository接口实现依赖倒置
- **应用代码可测试**：可以轻松使用Mock Repository进行单元测试
- **符合Clean Architecture原则**：依赖方向由外向内

## 技术栈

- **Java 17+**
- **Spring Boot 3.5.8**
- **Maven** - 项目构建工具
- **Lombok** - 简化Java代码
- **SLF4J/Logback** - 日志框架
- **Jackson** - JSON处理

## 项目结构

```
src/main/java/com/yuki/server/
├── presentation/              # 表示层
│   └── controller/           # REST API控制器
├── application/              # 应用层
│   ├── service/              # 服务接口
│   │   └── impl/             # 服务实现类
│   ├── repository/           # Repository接口
│   └── dto/                  # 数据传输对象
├── domain/                   # 领域层
│   ├── model/                # 领域模型
│   └── exception/            # 领域异常
└── infrastructure/           # 基础设施层
    ├── repository/           # Repository实现
    ├── store/                # 数据存储（Map实现）
    ├── config/               # 配置类
    └── exception/            # 异常处理器
```

## 核心功能

### 1. 产品管理
- 获取所有产品列表
- 从JSON文件自动加载产品数据

### 2. 购物车管理
- 创建空购物车
- 添加产品到购物车
- 应用折扣码
- 查看购物车详情

### 3. 订单处理
- 将购物车转换为订单（结账）
- 查看订单列表
- 按客户ID查询订单

### 4. 支付验证
- Luhn算法验证信用卡号
- 验证信用卡过期日期
- 支付处理

## 详细类说明

### 表示层 (Presentation Layer)

#### `ProductController.java`
- **位置**: `presentation/controller/`
- **功能**: 处理产品相关的HTTP请求
- **端点**: `GET /api/products`
- **依赖**: `ProductService`（应用层接口）

#### `BasketController.java`
- **位置**: `presentation/controller/`
- **功能**: 处理购物车相关的HTTP请求
- **端点**: 
  - `POST /api/baskets?customerId=1234`
  - `GET /api/baskets/{basketId}`
  - `PUT /api/baskets/{basketId}/products`
  - `PUT /api/baskets/{basketId}/discount`
- **依赖**: `BasketService`（应用层接口）

#### `OrderController.java`
- **位置**: `presentation/controller/`
- **功能**: 处理订单相关的HTTP请求
- **端点**: 
  - `POST /api/orders/checkout/{basketId}`
  - `GET /api/orders`
  - `GET /api/orders/customer/{customerId}`
- **依赖**: `OrderService`（应用层接口）

### 应用层 (Application Layer)

#### 服务接口和实现

**`ProductService` 接口**
- **位置**: `application/service/`
- **功能**: 产品服务接口
- **方法**: `getAllProducts()`, `getProductByCode()`, `getAllProductsMap()`
- **依赖**: `ProductRepository`（Repository接口）

**`ProductServiceImpl` 实现类**
- **位置**: `application/service/impl/`
- **功能**: 产品服务实现
- **依赖**: `ProductRepository`（接口，不依赖具体实现）

**`BasketService` 接口和实现**
- **位置**: `application/service/` 和 `application/service/impl/`
- **功能**: 购物车服务
- **依赖**: `BasketRepository`, `ProductRepository`, `PromotionRepository`（都是接口）

**`OrderService` 接口和实现**
- **位置**: `application/service/` 和 `application/service/impl/`
- **功能**: 订单服务，包含结账逻辑和日志记录
- **依赖**: `OrderRepository`, `BasketRepository`, `ProductRepository`, `PromotionRepository`, `CreditCardService`（都是接口）

**`CreditCardService` 接口和实现**
- **位置**: `application/service/` 和 `application/service/impl/`
- **功能**: 信用卡验证服务，实现Luhn算法和过期日期验证
- **依赖**: 无外部依赖（纯业务逻辑）

#### Repository接口

**`ProductRepository` 接口**
- **位置**: `application/repository/`
- **功能**: 产品数据访问接口
- **方法**: `save()`, `findByCode()`, `findAll()`
- **实现**: `ProductRepositoryImpl`（基础设施层）

**`BasketRepository` 接口**
- **位置**: `application/repository/`
- **功能**: 购物车数据访问接口
- **方法**: `save()`, `findById()`, `findByCustomerId()`, `delete()`
- **实现**: `BasketRepositoryImpl`（基础设施层）

**`OrderRepository` 接口**
- **位置**: `application/repository/`
- **功能**: 订单数据访问接口
- **方法**: `save()`, `findById()`, `findAll()`, `findByCustomerId()`
- **实现**: `OrderRepositoryImpl`（基础设施层）

**`PromotionRepository` 接口**
- **位置**: `application/repository/`
- **功能**: 促销数据访问接口
- **方法**: `save()`, `findByCode()`, `isValid()`
- **实现**: `PromotionRepositoryImpl`（基础设施层）

#### DTO（数据传输对象）

所有DTO位于`application/dto/`包中：
- `ProductResponse`: 产品响应
- `BasketResponse`: 购物车响应
- `OrderResponse`: 订单响应
- `AddProductRequest`: 添加产品请求
- `ApplyDiscountRequest`: 应用折扣请求
- `CheckoutRequest`: 结账请求
- `ErrorResponse`: 错误响应

### 领域层 (Domain Layer)

#### 领域模型

**`Product.java`**
- **位置**: `domain/model/`
- **功能**: 产品领域模型
- **字段**: `productCode`, `name`, `fullPrice`
- **特点**: 不可变对象，使用`final`字段

**`Basket.java`**
- **位置**: `domain/model/`
- **功能**: 购物车领域模型
- **字段**: `basketId`, `customerId`, `products`, `discountCode`
- **核心方法**: `addProduct()`, `calculateTotalPrice()`

**`Order.java`**
- **位置**: `domain/model/`
- **功能**: 订单领域模型
- **字段**: `orderId`, `customerId`, `totalPrice`, `createdAt`, `discountCode`

**`Promotion.java`**
- **位置**: `domain/model/`
- **功能**: 促销领域模型
- **字段**: `discountCode`, `discountPercent`

**`Customer.java`**
- **位置**: `domain/model/`
- **功能**: 客户领域模型
- **字段**: `customerId`

#### 领域异常

**`ResourceNotFoundException.java`**
- **位置**: `domain/exception/`
- **功能**: 资源未找到异常

**`InvalidRequestException.java`**
- **位置**: `domain/exception/`
- **功能**: 无效请求异常

**`PaymentException.java`**
- **位置**: `domain/exception/`
- **功能**: 支付异常

### 基础设施层 (Infrastructure Layer)

#### Repository实现

**`ProductRepositoryImpl.java`**
- **位置**: `infrastructure/repository/`
- **功能**: 产品Repository实现
- **依赖**: `ProductStore`（基础设施层）
- **实现**: `ProductRepository`接口

**`BasketRepositoryImpl.java`**
- **位置**: `infrastructure/repository/`
- **功能**: 购物车Repository实现
- **依赖**: `BasketStore`（基础设施层）
- **实现**: `BasketRepository`接口

**`OrderRepositoryImpl.java`**
- **位置**: `infrastructure/repository/`
- **功能**: 订单Repository实现
- **依赖**: `OrderStore`（基础设施层）
- **实现**: `OrderRepository`接口

**`PromotionRepositoryImpl.java`**
- **位置**: `infrastructure/repository/`
- **功能**: 促销Repository实现
- **依赖**: `PromotionStore`（基础设施层）
- **实现**: `PromotionRepository`接口

#### 数据存储 (Store)

所有Store类位于`infrastructure/store/`包中，使用`ConcurrentHashMap`实现线程安全的内存存储：

- **`ProductStore`**: 产品数据存储
- **`BasketStore`**: 购物车数据存储
- **`OrderStore`**: 订单数据存储
- **`PromotionStore`**: 促销数据存储

#### 配置类

**`DataLoader.java`**
- **位置**: `infrastructure/config/`
- **功能**: 应用启动时从`data.json`加载产品数据和折扣码数据
- **依赖**: `ProductRepository`, `PromotionRepository`（应用层接口）
- **实现**: `CommandLineRunner`接口

#### 异常处理

**`GlobalExceptionHandler.java`**
- **位置**: `infrastructure/exception/`
- **功能**: 全局异常处理器
- **实现**: 使用`@RestControllerAdvice`注解，处理领域异常并返回统一错误响应

## 设计模式

### 1. Repository Pattern（仓储模式）
- **目的**: 将数据访问逻辑与业务逻辑分离
- **实现**: Repository接口定义在应用层，实现类在基础设施层
- **优势**: 
  - 应用层不依赖具体的数据存储实现
  - 可以轻松替换数据存储方式（如从Map改为数据库）
  - 便于单元测试（可以使用Mock Repository）

### 2. Dependency Injection（依赖注入）
- **实现**: 使用Spring的构造函数注入
- **优势**: 
  - 降低耦合度
  - 提高可测试性
  - 符合依赖倒置原则

### 3. Interface Segregation（接口隔离）
- **实现**: Service层和Repository层都使用接口
- **优势**: 
  - 清晰的契约定义
  - 便于实现多种实现方式
  - 提高代码可维护性

## API使用示例

### 1. 获取产品列表
```bash
GET http://localhost:8080/api/products
```

### 2. 创建购物车
```bash
POST http://localhost:8080/api/baskets?customerId=1234
```

### 3. 添加产品到购物车
```bash
PUT http://localhost:8080/api/baskets/{basketId}/products
Content-Type: application/json

{
  "productCode": "PROD1",
  "quantity": 2
}
```

### 4. 应用折扣码
```bash
PUT http://localhost:8080/api/baskets/{basketId}/discount
Content-Type: application/json

{
  "discountCode": "DISCOUNT10"
}
```

### 5. 结账
```bash
POST http://localhost:8080/api/orders/checkout/{basketId}
Content-Type: application/json

{
  "cardNumber": "4000056655665556",
  "expiryDate": "01/27"
}
```

### 6. 获取订单列表
```bash
GET http://localhost:8080/api/orders
```

## 数据文件

### `data.json`
位于`src/main/resources/static/data.json`，包含：
- **products**: 产品列表
- **promotions**: 折扣码列表

示例：
```json
{
  "products": [
    {
      "productCode": "PROD1",
      "name": "Laptop",
      "fullPrice": 999.99
    }
  ],
  "promotions": [
    {
      "discountCode": "DISCOUNT10",
      "discountPercent": 10
    }
  ]
}
```

## 运行项目

### 前置要求
- Java 17或更高版本
- Maven 3.6+

### 启动步骤
1. 克隆项目
2. 进入项目目录
3. 运行Maven命令：
   ```bash
   mvn spring-boot:run
   ```
4. 应用将在 `http://localhost:8080` 启动

### 测试
运行测试：
```bash
mvn test
```

## Clean Architecture优势

1. **可测试性**: 应用层代码不依赖基础设施，可以轻松使用Mock对象进行单元测试
2. **可维护性**: 清晰的层次划分，职责明确
3. **可扩展性**: 可以轻松替换基础设施实现（如从Map改为数据库）
4. **独立性**: 业务逻辑独立于框架、UI和数据库
5. **依赖方向**: 依赖方向由外向内，符合依赖倒置原则

## 注意事项

1. **金额计算**: 使用`BigDecimal`确保精确计算，避免浮点数精度问题
2. **线程安全**: Store类使用`ConcurrentHashMap`支持并发访问
3. **数据持久化**: 当前使用内存存储，重启后数据会丢失
4. **信用卡验证**: 使用Luhn算法验证，支持13-19位卡号
5. **日期格式**: 过期日期格式为`MM/yy`（如`01/27`）

## 扩展建议

1. **数据库集成**: 将Map存储替换为数据库（如MySQL、PostgreSQL），只需实现新的Repository实现类
2. **缓存机制**: 添加Redis缓存提高性能
3. **认证授权**: 添加JWT认证
4. **API文档**: 集成Swagger/OpenAPI
5. **单元测试**: 添加完整的单元测试和集成测试
6. **分布式锁**: 添加分布式锁防止并发问题

## 作者

Yuki

## 许可证

本项目为课程作业项目。
