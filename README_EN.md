# E-Commerce Shopping Cart System

This is an e-commerce shopping cart web service system based on Spring Boot, designed with Clean Architecture principles. It implements product management, shopping cart management, order processing, and payment validation.

## Project Overview

This project is a complete e-commerce shopping cart system backend service, designed with Clean Architecture layered architecture, following Google Java coding guidelines, and developed using Java 17 and Spring Boot 3.5.8.

## Architecture Design

This project follows **Clean Architecture** principles, dividing the code into four main layers:

1. **Presentation Layer**: Handles HTTP requests and responses
2. **Application Layer**: Contains business logic, service interfaces and implementations, Repository interfaces, and DTOs
3. **Domain Layer**: Contains domain models and domain exceptions
4. **Infrastructure Layer**: Contains Repository implementations, data storage, configuration, and exception handling

### Dependency Direction

```
Presentation → Application → Domain
Infrastructure → Application (via Repository interfaces)
```

- **Application layer does not depend on Infrastructure layer**: Dependency inversion is achieved through Repository interfaces
- **Application code is testable**: Easy to unit test with Mock Repositories
- **Follows Clean Architecture principles**: Dependencies point inward

## Technology Stack

- **Java 17+**
- **Spring Boot 3.5.8**
- **Maven** - Project build tool
- **Lombok** - Simplifies Java code
- **SLF4J/Logback** - Logging framework
- **Jackson** - JSON processing

## Project Structure

```
src/main/java/com/yuki/server/
├── presentation/              # Presentation Layer
│   └── controller/           # REST API Controllers
├── application/              # Application Layer
│   ├── service/              # Service interfaces
│   │   └── impl/             # Service implementations
│   ├── repository/           # Repository interfaces
│   └── dto/                  # Data Transfer Objects
├── domain/                   # Domain Layer
│   ├── model/                # Domain models
│   └── exception/            # Domain exceptions
└── infrastructure/           # Infrastructure Layer
    ├── repository/           # Repository implementations
    ├── store/                # Data storage (Map implementation)
    ├── config/               # Configuration classes
    └── exception/            # Exception handlers
```

## Core Features

### 1. Product Management
- Get all products list
- Automatically load product data from JSON file

### 2. Shopping Cart Management
- Create empty shopping cart
- Add products to cart
- Apply discount codes
- View cart details

### 3. Order Processing
- Convert cart to order (checkout)
- View order list
- Query orders by customer ID

### 4. Payment Validation
- Luhn algorithm for credit card number validation
- Credit card expiry date validation
- Payment processing

## Detailed Class Documentation

### Presentation Layer

#### `ProductController.java`
- **Location**: `presentation/controller/`
- **Function**: Handles product-related HTTP requests
- **Endpoints**: `GET /api/products`
- **Dependencies**: `ProductService` (Application layer interface)

#### `BasketController.java`
- **Location**: `presentation/controller/`
- **Function**: Handles shopping cart-related HTTP requests
- **Endpoints**: 
  - `POST /api/baskets?customerId=1234`
  - `GET /api/baskets/{basketId}`
  - `PUT /api/baskets/{basketId}/products`
  - `PUT /api/baskets/{basketId}/discount`
- **Dependencies**: `BasketService` (Application layer interface)

#### `OrderController.java`
- **Location**: `presentation/controller/`
- **Function**: Handles order-related HTTP requests
- **Endpoints**: 
  - `POST /api/orders/checkout/{basketId}`
  - `GET /api/orders`
  - `GET /api/orders/customer/{customerId}`
- **Dependencies**: `OrderService` (Application layer interface)

### Application Layer

#### Service Interfaces and Implementations

**`ProductService` Interface**
- **Location**: `application/service/`
- **Function**: Product service interface
- **Methods**: `getAllProducts()`, `getProductByCode()`, `getAllProductsMap()`
- **Dependencies**: `ProductRepository` (Repository interface)

**`ProductServiceImpl` Implementation**
- **Location**: `application/service/impl/`
- **Function**: Product service implementation
- **Dependencies**: `ProductRepository` (interface, not concrete implementation)

**`BasketService` Interface and Implementation**
- **Location**: `application/service/` and `application/service/impl/`
- **Function**: Shopping cart service
- **Dependencies**: `BasketRepository`, `ProductRepository`, `PromotionRepository` (all interfaces)

**`OrderService` Interface and Implementation**
- **Location**: `application/service/` and `application/service/impl/`
- **Function**: Order service, includes checkout logic and logging
- **Dependencies**: `OrderRepository`, `BasketRepository`, `ProductRepository`, `PromotionRepository`, `CreditCardService` (all interfaces)

**`CreditCardService` Interface and Implementation**
- **Location**: `application/service/` and `application/service/impl/`
- **Function**: Credit card validation service, implements Luhn algorithm and expiry date validation
- **Dependencies**: No external dependencies (pure business logic)

#### Repository Interfaces

**`ProductRepository` Interface**
- **Location**: `application/repository/`
- **Function**: Product data access interface
- **Methods**: `save()`, `findByCode()`, `findAll()`
- **Implementation**: `ProductRepositoryImpl` (Infrastructure layer)

**`BasketRepository` Interface**
- **Location**: `application/repository/`
- **Function**: Shopping cart data access interface
- **Methods**: `save()`, `findById()`, `findByCustomerId()`, `delete()`
- **Implementation**: `BasketRepositoryImpl` (Infrastructure layer)

**`OrderRepository` Interface**
- **Location**: `application/repository/`
- **Function**: Order data access interface
- **Methods**: `save()`, `findById()`, `findAll()`, `findByCustomerId()`
- **Implementation**: `OrderRepositoryImpl` (Infrastructure layer)

**`PromotionRepository` Interface**
- **Location**: `application/repository/`
- **Function**: Promotion data access interface
- **Methods**: `save()`, `findByCode()`, `isValid()`
- **Implementation**: `PromotionRepositoryImpl` (Infrastructure layer)

#### DTOs (Data Transfer Objects)

All DTOs are located in the `application/dto/` package:
- `ProductResponse`: Product response
- `BasketResponse`: Shopping cart response
- `OrderResponse`: Order response
- `AddProductRequest`: Add product request
- `ApplyDiscountRequest`: Apply discount request
- `CheckoutRequest`: Checkout request
- `ErrorResponse`: Error response

### Domain Layer

#### Domain Models

**`Product.java`**
- **Location**: `domain/model/`
- **Function**: Product domain model
- **Fields**: `productCode`, `name`, `fullPrice`
- **Characteristics**: Immutable object with `final` fields

**`Basket.java`**
- **Location**: `domain/model/`
- **Function**: Shopping cart domain model
- **Fields**: `basketId`, `customerId`, `products`, `discountCode`
- **Core Methods**: `addProduct()`, `calculateTotalPrice()`

**`Order.java`**
- **Location**: `domain/model/`
- **Function**: Order domain model
- **Fields**: `orderId`, `customerId`, `totalPrice`, `createdAt`, `discountCode`

**`Promotion.java`**
- **Location**: `domain/model/`
- **Function**: Promotion domain model
- **Fields**: `discountCode`, `discountPercent`

**`Customer.java`**
- **Location**: `domain/model/`
- **Function**: Customer domain model
- **Fields**: `customerId`

#### Domain Exceptions

**`ResourceNotFoundException.java`**
- **Location**: `domain/exception/`
- **Function**: Resource not found exception

**`InvalidRequestException.java`**
- **Location**: `domain/exception/`
- **Function**: Invalid request exception

**`PaymentException.java`**
- **Location**: `domain/exception/`
- **Function**: Payment exception

### Infrastructure Layer

#### Repository Implementations

**`ProductRepositoryImpl.java`**
- **Location**: `infrastructure/repository/`
- **Function**: Product Repository implementation
- **Dependencies**: `ProductStore` (Infrastructure layer)
- **Implements**: `ProductRepository` interface

**`BasketRepositoryImpl.java`**
- **Location**: `infrastructure/repository/`
- **Function**: Shopping cart Repository implementation
- **Dependencies**: `BasketStore` (Infrastructure layer)
- **Implements**: `BasketRepository` interface

**`OrderRepositoryImpl.java`**
- **Location**: `infrastructure/repository/`
- **Function**: Order Repository implementation
- **Dependencies**: `OrderStore` (Infrastructure layer)
- **Implements**: `OrderRepository` interface

**`PromotionRepositoryImpl.java`**
- **Location**: `infrastructure/repository/`
- **Function**: Promotion Repository implementation
- **Dependencies**: `PromotionStore` (Infrastructure layer)
- **Implements**: `PromotionRepository` interface

#### Data Storage (Store)

All Store classes are located in the `infrastructure/store/` package, using `ConcurrentHashMap` for thread-safe in-memory storage:

- **`ProductStore`**: Product data storage
- **`BasketStore`**: Shopping cart data storage
- **`OrderStore`**: Order data storage
- **`PromotionStore`**: Promotion data storage

#### Configuration Classes

**`DataLoader.java`**
- **Location**: `infrastructure/config/`
- **Function**: Loads product and promotion data from `data.json` on application startup
- **Dependencies**: `ProductRepository`, `PromotionRepository` (Application layer interfaces)
- **Implements**: `CommandLineRunner` interface

#### Exception Handling

**`GlobalExceptionHandler.java`**
- **Location**: `infrastructure/exception/`
- **Function**: Global exception handler
- **Implementation**: Uses `@RestControllerAdvice` annotation to handle domain exceptions and return unified error responses

## Design Patterns

### 1. Repository Pattern
- **Purpose**: Separates data access logic from business logic
- **Implementation**: Repository interfaces defined in Application layer, implementations in Infrastructure layer
- **Benefits**: 
  - Application layer does not depend on concrete data storage implementations
  - Easy to replace data storage (e.g., from Map to database)
  - Facilitates unit testing (can use Mock Repositories)

### 2. Dependency Injection
- **Implementation**: Uses Spring's constructor injection
- **Benefits**: 
  - Reduces coupling
  - Improves testability
  - Follows Dependency Inversion Principle

### 3. Interface Segregation
- **Implementation**: Both Service and Repository layers use interfaces
- **Benefits**: 
  - Clear contract definitions
  - Easy to implement multiple implementations
  - Improves code maintainability

## API Usage Examples

### 1. Get All Products
```bash
GET http://localhost:8080/api/products
```

### 2. Create Shopping Cart
```bash
POST http://localhost:8080/api/baskets?customerId=1234
```

### 3. Add Product to Cart
```bash
PUT http://localhost:8080/api/baskets/{basketId}/products
Content-Type: application/json

{
  "productCode": "PROD1",
  "quantity": 2
}
```

### 4. Apply Discount Code
```bash
PUT http://localhost:8080/api/baskets/{basketId}/discount
Content-Type: application/json

{
  "discountCode": "DISCOUNT10"
}
```

### 5. Checkout
```bash
POST http://localhost:8080/api/orders/checkout/{basketId}
Content-Type: application/json

{
  "cardNumber": "4000056655665556",
  "expiryDate": "01/27"
}
```

### 6. Get All Orders
```bash
GET http://localhost:8080/api/orders
```

## Data File

### `data.json`
Located at `src/main/resources/static/data.json`, contains:
- **products**: Product list
- **promotions**: Discount code list

Example:
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

## Running the Project

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Startup Steps
1. Clone the project
2. Navigate to the project directory
3. Run Maven command:
   ```bash
   mvn spring-boot:run
   ```
4. Application will start at `http://localhost:8080`

### Testing
Run tests:
```bash
mvn test
```

## Clean Architecture Benefits

1. **Testability**: Application layer code does not depend on infrastructure, making it easy to unit test with Mock objects
2. **Maintainability**: Clear layer separation with well-defined responsibilities
3. **Extensibility**: Easy to replace infrastructure implementations (e.g., from Map to database)
4. **Independence**: Business logic is independent of frameworks, UI, and databases
5. **Dependency Direction**: Dependencies point inward, following Dependency Inversion Principle

## Important Notes

1. **Amount Calculation**: Uses `BigDecimal` for precise calculations, avoiding floating-point precision issues
2. **Thread Safety**: Store classes use `ConcurrentHashMap` to support concurrent access
3. **Data Persistence**: Currently uses in-memory storage, data will be lost after restart
4. **Credit Card Validation**: Uses Luhn algorithm for validation, supports 13-19 digit card numbers
5. **Date Format**: Expiry date format is `MM/yy` (e.g., `01/27`)

## Extension Suggestions

1. **Database Integration**: Replace Map storage with database (e.g., MySQL, PostgreSQL), only need to implement new Repository implementation classes
2. **Caching Mechanism**: Add Redis caching to improve performance
3. **Authentication & Authorization**: Add JWT authentication
4. **API Documentation**: Integrate Swagger/OpenAPI
5. **Unit Testing**: Add comprehensive unit tests and integration tests
6. **Distributed Locking**: Add distributed locks to prevent concurrency issues

## Author

Yuki

## License

This project is a course assignment project.

