# 电商购物车系统

这是一个基于Spring Boot的电商购物车Web服务系统，实现了产品管理、购物车管理、订单处理和支付验证等功能。

## 项目概述

本项目是一个完整的电商购物车系统后端服务，采用分层架构设计，遵循Google Java编码规范，使用Java 17和Spring Boot 3.5.8框架开发。

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
├── config/              # 配置类
├── contoller/           # 控制器层（REST API）
├── dto/                 # 数据传输对象
├── exception/           # 异常处理
├── mapper/              # 数据存储层
├── model/               # 实体模型
└── service/            # 服务层
    └── impl/            # 服务实现类
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

### 模型层 (Model)

#### `Product.java`
- **功能**: 表示可购买的产品实体
- **字段**:
  - `productCode`: 产品代码（唯一标识）
  - `name`: 产品名称
  - `fullPrice`: 产品全价（BigDecimal，精确计算）
- **实现方式**: 使用Lombok的`@Data`注解自动生成getter/setter，字段使用`final`确保不可变性

#### `Customer.java`
- **功能**: 表示想要购买产品的客户
- **字段**:
  - `customerId`: 客户ID（唯一标识）
- **实现方式**: 简单的值对象，使用Lombok简化代码

#### `Promotion.java`
- **功能**: 表示折扣码和折扣百分比
- **字段**:
  - `discountCode`: 折扣码（如DISCOUNT10、DISCOUNT20）
  - `discountPercent`: 折扣百分比（BigDecimal）
- **实现方式**: 不可变对象，用于计算折扣金额

#### `Basket.java`
- **功能**: 持有用户想要购买的0..*个产品
- **字段**:
  - `basketId`: 购物车ID（唯一标识）
  - `customerId`: 所属客户ID
  - `products`: 产品映射（productCode -> quantity）
  - `discountCode`: 应用的折扣码
- **核心方法**:
  - `addProduct()`: 添加产品到购物车
  - `calculateTotalPrice()`: 计算总价（不含折扣）
- **实现方式**: 使用`HashMap`存储产品，支持并发访问

#### `Order.java`
- **功能**: 当使用支付购买Basket时保存的对象
- **字段**:
  - `orderId`: 订单ID（唯一标识）
  - `customerId`: 客户ID
  - `totalPrice`: 总价（已应用折扣）
  - `createdAt`: 创建时间
  - `discountCode`: 使用的折扣码
- **实现方式**: 不可变对象，记录订单快照

### 数据传输对象层 (DTO)

#### 请求DTO
- **`AddProductRequest.java`**: 添加产品到购物车的请求
  - `productCode`: 产品代码
  - `quantity`: 数量

- **`ApplyDiscountRequest.java`**: 应用折扣码的请求
  - `discountCode`: 折扣码

- **`CheckoutRequest.java`**: 结账请求
  - `cardNumber`: 信用卡号
  - `expiryDate`: 过期日期（MM/yy格式）

#### 响应DTO
- **`ProductResponse.java`**: 产品响应
- **`BasketResponse.java`**: 购物车响应（包含总价）
- **`OrderResponse.java`**: 订单响应
- **`ErrorResponse.java`**: 错误响应
  - `error`: 错误类型
  - `message`: 错误消息

### 数据存储层 (Mapper/Store)

所有Store类使用`ConcurrentHashMap`实现线程安全的内存存储。

#### `ProductStore.java`
- **功能**: 产品数据存储
- **方法**:
  - `save()`: 保存产品
  - `findByCode()`: 根据产品代码查找
  - `findAll()`: 获取所有产品
- **实现方式**: 使用`ConcurrentHashMap<String, Product>`存储，key为productCode

#### `BasketStore.java`
- **功能**: 购物车数据存储
- **方法**:
  - `save()`: 保存购物车
  - `findById()`: 根据ID查找
  - `findByCustomerId()`: 根据客户ID查找
  - `delete()`: 删除购物车
- **实现方式**: 使用`ConcurrentHashMap<String, Basket>`存储，key为basketId

#### `OrderStore.java`
- **功能**: 订单数据存储
- **方法**:
  - `save()`: 保存订单
  - `findById()`: 根据ID查找
  - `findAll()`: 获取所有订单
  - `findByCustomerId()`: 根据客户ID查找订单列表
- **实现方式**: 使用`ConcurrentHashMap<String, Order>`存储，key为orderId

#### `PromotionStore.java`
- **功能**: 促销/折扣码数据存储
- **方法**:
  - `save()`: 保存折扣码
  - `findByCode()`: 根据折扣码查找
  - `isValid()`: 验证折扣码是否有效
- **实现方式**: 使用`ConcurrentHashMap<String, Promotion>`存储，key为discountCode
- **数据来源**: 从`data.json`文件加载，不再硬编码

### 服务层 (Service)

服务层采用**接口+实现类**的设计模式，提高代码的可维护性和可测试性。

#### `ProductService` 接口
- **功能**: 产品服务接口
- **方法**:
  - `getAllProducts()`: 获取所有产品列表
  - `getProductByCode()`: 根据产品代码获取产品
  - `getAllProductsMap()`: 获取所有产品映射

#### `ProductServiceImpl` 实现类
- **功能**: 产品服务实现
- **依赖**: `ProductStore`
- **实现方式**: 
  - 从ProductStore获取数据
  - 转换为DTO对象返回
  - 处理资源未找到异常

#### `BasketService` 接口
- **功能**: 购物车服务接口
- **方法**:
  - `createBasket()`: 创建空购物车
  - `getBasket()`: 获取购物车
  - `getBasketByCustomerId()`: 根据客户ID获取购物车
  - `addProduct()`: 添加产品到购物车
  - `applyDiscount()`: 应用折扣码

#### `BasketServiceImpl` 实现类
- **功能**: 购物车服务实现
- **依赖**: `BasketStore`, `ProductStore`, `PromotionStore`, `ProductService`
- **核心逻辑**:
  - 创建购物车时生成UUID作为basketId
  - 添加产品时验证产品存在性和数量有效性
  - 应用折扣码时验证折扣码有效性
  - 计算总价时自动应用折扣（如果存在）
- **实现方式**: 
  - 使用`BigDecimal`进行精确的金额计算
  - 折扣计算：`总价 - (总价 × 折扣百分比 / 100)`

#### `OrderService` 接口
- **功能**: 订单服务接口
- **方法**:
  - `checkout()`: 将购物车转换为订单（结账）
  - `getAllOrders()`: 获取所有订单
  - `getOrdersByCustomerId()`: 根据客户ID获取订单

#### `OrderServiceImpl` 实现类
- **功能**: 订单服务实现
- **依赖**: `OrderStore`, `BasketStore`, `ProductStore`, `PromotionStore`, `CreditCardService`
- **核心逻辑**:
  - 结账流程：
    1. 验证购物车存在
    2. 验证支付信息（信用卡号和过期日期）
    3. 计算总价（含折扣）
    4. 创建订单
    5. 保存订单
    6. 删除购物车
    7. 记录日志
- **日志记录**: 使用SLF4J记录购物车到订单的成功转换，包含basketId、orderId、customerId和总价

#### `CreditCardService` 接口
- **功能**: 信用卡验证服务接口
- **方法**:
  - `isValidCardNumber()`: 验证信用卡号
  - `isValidExpiryDate()`: 验证过期日期
  - `processPayment()`: 处理支付

#### `CreditCardServiceImpl` 实现类
- **功能**: 信用卡验证服务实现
- **核心算法**:
  - **Luhn算法验证**:
    1. 移除所有非数字字符
    2. 验证长度（13-19位）
    3. 从右到左处理每个数字
    4. 偶数位数字乘以2，如果结果大于9则减去9
    5. 所有数字求和
    6. 如果和能被10整除，则通过验证
  - **过期日期验证**:
    1. 解析MM/yy格式
    2. 转换为完整日期（该月最后一天）
    3. 验证日期必须晚于或等于今天
- **异常处理**: 验证失败时抛出`PaymentException`

### 控制器层 (Controller)

#### `ProductController.java`
- **路径**: `/api/products`
- **端点**:
  - `GET /api/products`: 获取所有产品列表
- **实现方式**: 使用`@RestController`和`@RequestMapping`注解，返回`ResponseEntity`

#### `BasketController.java`
- **路径**: `/api/baskets`
- **端点**:
  - `POST /api/baskets?customerId=1234`: 创建空购物车
  - `GET /api/baskets/{basketId}`: 获取购物车
  - `GET /api/baskets?customerId=1234`: 根据客户ID获取购物车
  - `PUT /api/baskets/{basketId}/products`: 添加产品到购物车
  - `PUT /api/baskets/{basketId}/discount`: 应用折扣码
- **实现方式**: RESTful API设计，使用HTTP方法语义化

#### `OrderController.java`
- **路径**: `/api/orders`
- **端点**:
  - `POST /api/orders/checkout/{basketId}`: 结账（Basket转Order）
  - `GET /api/orders`: 获取所有订单列表
  - `GET /api/orders/customer/{customerId}`: 根据客户ID获取订单
- **实现方式**: 结账使用POST方法，查询使用GET方法

### 异常处理

#### 异常类
- **`ResourceNotFoundException.java`**: 资源未找到异常（404）
- **`InvalidRequestException.java`**: 无效请求异常（400）
- **`PaymentException.java`**: 支付异常（400）

#### `GlobalExceptionHandler.java`
- **功能**: 全局异常处理器
- **实现方式**: 
  - 使用`@RestControllerAdvice`注解
  - 为每种异常类型定义处理方法
  - 返回统一的`ErrorResponse`格式
  - 记录错误日志
- **HTTP状态码映射**:
  - `ResourceNotFoundException` → 404 Not Found
  - `InvalidRequestException` → 400 Bad Request
  - `PaymentException` → 400 Bad Request
  - 其他异常 → 500 Internal Server Error

### 配置类

#### `DataLoader.java`
- **功能**: 应用启动时从`data.json`加载产品数据和折扣码数据
- **实现方式**:
  - 实现`CommandLineRunner`接口
  - 在`run()`方法中读取JSON文件
  - 解析JSON数据并保存到对应的Store
  - 使用Jackson的`ObjectMapper`解析JSON
  - 记录加载日志
- **数据格式**: 
  ```json
  {
    "products": [...],
    "promotions": [...]
  }
  ```

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

## 架构设计

### 分层架构
1. **Controller层**: 处理HTTP请求，参数验证
2. **Service层**: 业务逻辑处理，使用接口+实现类模式
3. **Mapper/Store层**: 数据存储，使用Map实现
4. **Model层**: 实体对象

### 设计模式
- **接口隔离原则**: Service层使用接口定义契约
- **依赖注入**: 使用Spring的构造函数注入
- **单一职责原则**: 每个类只负责一个功能
- **异常处理**: 统一的异常处理机制

### 线程安全
- 所有Store使用`ConcurrentHashMap`确保线程安全
- 实体对象使用不可变设计（final字段）

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

## 日志配置

日志配置在`application.yml`中：
- 控制台输出：UTF-8编码
- 文件输出：`./logs/app.log`
- 日志级别：INFO（根级别）

## 注意事项

1. **金额计算**: 使用`BigDecimal`确保精确计算，避免浮点数精度问题
2. **线程安全**: Store类使用`ConcurrentHashMap`支持并发访问
3. **数据持久化**: 当前使用内存存储，重启后数据会丢失
4. **信用卡验证**: 使用Luhn算法验证，支持13-19位卡号
5. **日期格式**: 过期日期格式为`MM/yy`（如`01/27`）

## 扩展建议

1. **数据库集成**: 将Map存储替换为数据库（如MySQL、PostgreSQL）
2. **缓存机制**: 添加Redis缓存提高性能
3. **认证授权**: 添加JWT认证
4. **API文档**: 集成Swagger/OpenAPI
5. **单元测试**: 添加完整的单元测试和集成测试
6. **分布式锁**: 添加分布式锁防止并发问题

## 作者

Yuki

## 许可证

本项目为课程作业项目。

