# Design Patterns and Architectural Patterns in Food Ordering Web API

This document accurately categorizes the **design patterns** (GoF patterns), **architectural patterns**, and **design principles** implemented in the codebase.

## Important Distinction:
- **Design Patterns** (GoF): Reusable solutions to common problems in object-oriented design
- **Architectural Patterns**: High-level structures for organizing applications
- **Design Principles**: Fundamental guidelines for writing maintainable code

# PART 1: DESIGN PATTERNS (GoF Patterns)

These are actual design patterns from the Gang of Four (GoF) patterns.

## 1. **Repository Pattern** ✅
**Type:** Data Access Pattern (not GoF, but widely recognized design pattern)

**Location:** `com.foodordering.repository.*`

**Purpose:** Abstracts data access logic and provides a clean interface for database operations.

**Implementation:**
- All repositories extend `MongoRepository<T, ID>` from Spring Data MongoDB
- Provides CRUD operations out of the box
- Custom query methods defined using Spring Data method naming conventions
- Examples: `AccountRepository`, `CategoryRepository`, `MenuItemRepository`, `OrderRepository`

**Why it's a pattern:** Encapsulates data access logic, making it interchangeable and testable.

```java
@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    Optional<Account> findByUsername(String username);
    boolean existsByEmail(String email);
}
```

---

## 2. **Template Method Pattern** ✅
**Type:** Behavioral Pattern (GoF)

**Location:** `BaseEntity` class

**Purpose:** Defines the skeleton of an algorithm in a base class, letting subclasses override specific steps.

**Implementation:**
- `BaseEntity` provides common structure (id, timestamps, soft-delete methods)
- All entities extend `BaseEntity` and inherit the template
- Subclasses can override specific methods if needed
- The template defines the algorithm structure (entity lifecycle)

**Why it's Template Method:** BaseEntity defines the algorithm structure (entity structure), and subclasses follow this template.

```java
public abstract class BaseEntity {
    @Id
    private String id;
    protected Instant createdAt;
    protected Instant modifiedAt;
    
    public void softDelete() { this.isDeleted = true; }
    public void restore() { this.isDeleted = false; }
    // Template structure that all entities follow
}

public class Account extends BaseEntity {
    // Follows the template defined in BaseEntity
}
```

---

## 3. **Facade Pattern** ✅
**Type:** Structural Pattern (GoF)

**Location:** Service layer (`com.foodordering.service.*`)

**Purpose:** Provides a simplified interface to a complex subsystem (repositories, business logic).

**Implementation:**
- Services hide complexity of multiple repository operations
- Controllers interact with simple service methods
- Services coordinate multiple repositories (e.g., `OrderServiceImpl` uses `OrderRepository`, `AccountRepository`, `MenuItemRepository`)
- Simplifies the interface for clients (controllers)

**Why it's Facade:** Services provide a unified, simplified interface to a complex subsystem of repositories and business logic.

```java
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    // Facade: simplifies complex operations involving multiple repositories
    public OrderResponse create(OrderRequest request) {
        // Coordinates multiple repositories behind a simple interface
    }
}
```

---

## 4. **Adapter Pattern** ✅
**Type:** Structural Pattern (GoF)

**Location:** `MomoPayService` (`com.foodordering.integration.momo.*`)

**Purpose:** Converts the interface of a class (MoMo Pay API) into another interface (our payment service) that clients expect.

**Implementation:**
- `MomoPayService` adapts MoMo Pay's external API to our internal payment interface
- Provides a unified interface (`createPayment`, `verifyPaymentCallback`) that hides MoMo Pay's complexity
- Handles MoMo Pay-specific details (signature generation, API format) internally
- Allows our payment service to work with MoMo Pay without knowing its internal structure

**Why it's Adapter:** The service acts as an adapter between our payment system and MoMo Pay's external API, making incompatible interfaces work together.

```java
@Service
public class MomoPayService {
    // Adapts MoMo Pay API to our payment interface
    public MomoPaymentResponse createPayment(String orderId, Double amount, String orderInfo) {
        // Converts our payment request format to MoMo Pay's API format
        // Handles MoMo-specific signature generation, request formatting
        // Returns response in our format
    }
    
    public boolean verifyPaymentCallback(Map<String, String> callbackData) {
        // Adapts MoMo Pay callback format to our verification logic
    }
}
```

---

## 5. **Observer Pattern** ✅
**Type:** Behavioral Pattern (GoF)

**Location:** `MongoEntityListener`

**Purpose:** Defines a one-to-many dependency between objects so that when one object changes state, all dependents are notified.

**Implementation:**
- `MongoEntityListener` extends `AbstractMongoEventListener<BaseEntity>`
- Listens to MongoDB events (`BeforeConvertEvent`)
- Automatically updates audit timestamps when entities are saved
- Entities don't need to manually set timestamps

**Why it's Observer:** The listener observes MongoDB events and reacts to entity state changes.

```java
@Component
public class MongoEntityListener extends AbstractMongoEventListener<BaseEntity> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseEntity> event) {
        // Observes and reacts to entity conversion events
        BaseEntity entity = event.getSource();
        if (entity.createdAt == null) {
            entity.createdAt = Instant.now();
        }
        entity.modifiedAt = Instant.now();
    }
}
```

---

# PART 2: ARCHITECTURAL PATTERNS

These are high-level architectural patterns, not GoF design patterns.

## 5. **Service Layer Pattern** (Architectural)
**Location:** `com.foodordering.service.*` and `com.foodordering.service.impl.*`

**Purpose:** Separates business logic from controllers and provides a clean API for business operations.

**Implementation:**
- Service interfaces define contracts (`AccountService`, `OrderService`, etc.)
- Service implementations contain business logic (`AccountServiceImpl`, `OrderServiceImpl`, etc.)
- Services use repositories for data access
- Services handle validation, business rules, and data transformation

**Note:** This is an **architectural pattern**, not a GoF design pattern. It's part of layered architecture.

```java
public interface AccountService {
    AccountResponse create(AccountRequest request);
    AccountResponse getById(String id);
}

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
}
```

---

## 6. **DTO (Data Transfer Object) Pattern** (Architectural)
**Location:** `com.foodordering.dto.request.*` and `com.foodordering.dto.response.*`

**Purpose:** Separates internal entity models from external API contracts.

**Implementation:**
- Request DTOs (`*Request`) - for incoming data with validation
- Response DTOs (`*Response`) - for outgoing data (excludes sensitive fields like passwords)
- Entities remain internal, DTOs are exposed via API

**Note:** This is an **architectural/data pattern**, not a GoF design pattern. It's used for data transfer between layers.

```java
public class AccountRequest {
    @NotBlank
    @Email
    private String email;
}

public class AccountResponse {
    private String id;
    private String email;
    // No password field exposed!
}
```

---

## 7. **MVC (Model-View-Controller) Pattern** (Architectural)
**Location:** `com.foodordering.controller.*`

**Purpose:** Separates concerns between data (Model), presentation (View), and user input (Controller).

**Implementation:**
- **Model:** Entities (`Account`, `Order`, etc.) and DTOs
- **View:** JSON responses (REST API)
- **Controller:** REST controllers handle HTTP requests/responses

**Note:** This is an **architectural pattern**, not a GoF design pattern.

```java
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    
    @PostMapping
    public ResponseEntity<AccountResponse> create(@RequestBody AccountRequest request) {
        // ...
    }
}
```

---

# PART 3: DESIGN PRINCIPLES & TECHNIQUES

These are design principles and programming techniques, not design patterns.

## 8. **Dependency Injection** (Design Principle/Technique)
**Location:** Throughout the codebase using `@Autowired`

**Purpose:** Inverts control of object creation and dependencies.

**Implementation:**
- Spring Framework manages object lifecycle
- `@Autowired` annotation injects dependencies
- `@Service`, `@Repository`, `@Controller` annotations register components

**Note:** This is a **design principle/technique** (Inversion of Control), not a design pattern. It's a way to implement the Dependency Inversion Principle.

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository; // Injected by Spring
}
```

---

## 9. **Exception Handling Pattern** (Architectural Pattern)
**Location:** `GlobalExceptionHandler`

**Purpose:** Centralized exception handling.

**Implementation:**
- `@RestControllerAdvice` catches all exceptions
- Converts exceptions to standardized error responses
- Handles validation errors separately

**Note:** This is an **architectural pattern** for error handling, not a GoF design pattern.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(...) {
        // ...
    }
}
```

---

## ❌ Patterns NOT Actually Used:

### **Strategy Pattern** - NOT USED
**Why not:** While we have interfaces and implementations, we're not swapping strategies at runtime. This is more accurately **Dependency Inversion Principle** - depending on abstractions rather than concrete classes.

### **Builder Pattern** - NOT USED
**Why not:** We use setter methods, which is not the Builder pattern. The Builder pattern requires a Builder class with fluent methods that return the builder itself for chaining.

---

# PART 4: DESIGN PRINCIPLES APPLIED

### **Separation of Concerns**
- Controllers: Handle HTTP requests/responses
- Services: Business logic
- Repositories: Data access
- DTOs: Data transfer

### **Single Responsibility Principle**
- Each class has one reason to change
- Services handle one domain each
- Repositories handle one entity each

### **Dependency Inversion Principle**
- High-level modules (controllers) depend on abstractions (service interfaces)
- Low-level modules (repositories) are injected, not instantiated

### **DRY (Don't Repeat Yourself)**
- `BaseEntity` provides common functionality
- `MongoEntityListener` handles audit automatically
- Reusable service patterns

### **RESTful API Design**
- Resource-based URLs (`/api/accounts`, `/api/orders`)
- HTTP methods for operations (GET, POST, PUT, DELETE)
- Proper HTTP status codes
- Stateless communication

---

---

# SUMMARY - Matching Standard Design Pattern Categories

## ✅ Design Patterns Matching Your List:

### 🔄 Behavioral Patterns:
1. ✅ **Template Method Pattern** - BaseEntity defines template for all entities
   - **Category:** Behavioral (GoF)
   - **Location:** `BaseEntity` class
   - **Matches:** "Defines the skeleton of an algorithm and lets subclasses override steps"

2. ✅ **Observer Pattern** - MongoEntityListener observes entity events
   - **Category:** Behavioral (GoF)
   - **Location:** `MongoEntityListener`
   - **Matches:** "Establishes one-to-many dependency between objects for event notification"

### ⚙️ Structural Patterns:
3. ✅ **Facade Pattern** - Service layer provides simplified interface
   - **Category:** Structural (GoF)
   - **Location:** Service layer (`com.foodordering.service.*`)
   - **Matches:** "Provides a simplified interface to a complex subsystem"

4. ✅ **Adapter Pattern** - MomoPayService adapts MoMo Pay API
   - **Category:** Structural (GoF)
   - **Location:** `MomoPayService` (`com.foodordering.integration.momo.*`)
   - **Matches:** "Converts one interface into another expected by the client"

### 🏗️ Architectural Patterns:
5. ✅ **MVC (Model-View-Controller) Pattern** - Model-View-Controller architecture
   - **Category:** Architectural Pattern
   - **Location:** Controllers, Entities, DTOs
   - **Matches:** "Separates data (Model), UI (View), and logic (Controller)"

6. ✅ **Layered Architecture (n-tier)** - Organizes code into layers
   - **Category:** Architectural Pattern
   - **Location:** Entire codebase structure
   - **Matches:** "Organizes code into layers (Presentation, Business, Data)"
   - **Our Layers:**
     - Presentation Layer: Controllers
     - Business Layer: Services
     - Data Access Layer: Repositories
     - Domain Layer: Entities

### 📝 Additional Patterns (Not in Standard List):
6. ✅ **Repository Pattern** - Data access abstraction
   - **Type:** Data Access Pattern (widely recognized, not GoF)
   - **Location:** `com.foodordering.repository.*`

## ❌ Patterns NOT Used:
- **Creational:** Singleton, Factory Method, Abstract Factory, Builder, Prototype
- **Structural:** Bridge, Composite, Decorator, Flyweight, Proxy
- **Behavioral:** Chain of Responsibility, Command, Interpreter, Iterator, Mediator, Memento, State, Strategy, Visitor
- **Architectural:** Client-Server, Microservices, Event-Driven, MVVM, Pipe and Filter, SOA, Hexagonal, Event Sourcing/CQRS

## Design Principles:
1. ✅ **Dependency Injection** - Inversion of Control
2. ✅ **Separation of Concerns**
3. ✅ **Single Responsibility Principle**
4. ✅ **Dependency Inversion Principle**
5. ✅ **DRY (Don't Repeat Yourself)**

## Architecture:

The codebase follows **layered architecture** with clear separation:
1. **Presentation Layer:** Controllers (REST endpoints)
2. **Business Layer:** Services (business logic)
3. **Data Access Layer:** Repositories (database operations)
4. **Domain Layer:** Entities (domain models)

This architecture ensures:
- ✅ Maintainability
- ✅ Testability
- ✅ Scalability
- ✅ Security
- ✅ Clean code principles

---

## For Academic Purposes:

### ✅ Design Patterns to List (Matching Standard Categories):

**Behavioral Patterns:**
1. **Template Method Pattern** - `BaseEntity` defines algorithm skeleton
2. **Observer Pattern** - `MongoEntityListener` observes MongoDB events

**Structural Patterns:**
3. **Facade Pattern** - Service layer simplifies complex subsystem
4. **Adapter Pattern** - `MomoPayService` adapts MoMo Pay API to our payment interface

**Architectural Patterns:**
5. **MVC Pattern** - Model-View-Controller separation
6. **Layered Architecture** - n-tier architecture with clear layer separation

### 📊 Summary Table:

| Pattern | Category | Location | Matches Standard List? |
|---------|----------|----------|------------------------|
| Template Method | Behavioral (GoF) | BaseEntity | ✅ Yes |
| Observer | Behavioral (GoF) | MongoEntityListener | ✅ Yes |
| Facade | Structural (GoF) | Service Layer | ✅ Yes |
| Adapter | Structural (GoF) | MomoPayService | ✅ Yes |
| MVC | Architectural | Controllers/Entities | ✅ Yes |
| Layered Architecture | Architectural | Entire codebase | ✅ Yes |
| Repository | Data Access | Repositories | ⚠️ Not in standard list |

### ❌ Do NOT List These as "Design Patterns":
- Service Layer (architectural pattern, not GoF)
- DTO Pattern (architectural/data pattern, not GoF)
- Dependency Injection (design principle/technique, not a pattern)
- Strategy Pattern (not actually used - we depend on abstractions but don't swap strategies)
- Builder Pattern (not used - we use setters, not builder pattern)

