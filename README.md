#  Monk Commerce Coupon Engine — Backend Service

A scalable and extensible **Spring Boot** backend service designed to manage and apply multiple types of discount coupons for an e-commerce platform.

This project focuses on **clean architecture**, **extensibility**, and **production-grade backend practices** rather than implementing every business rule. The system is built using a Strategy-based design so new coupon types can be added with minimal changes.

---

#  Overview

The Coupon Engine exposes REST APIs to:

* Create and manage coupons
* Evaluate applicable coupons for a cart
* Apply discounts using different coupon strategies
* Support future promotion types with minimal code changes

Supported coupon types:

* Cart-wise coupons
* Product-wise coupons
* Buy-X-Get-Y (BXGY)

The design emphasizes separation of concerns and predictable API responses suitable for real production systems.

---

#  High Level Architecture

```
Controller → CouponEngineService → StrategyFactory → CouponStrategy
```

## Controller Layer

* Handles HTTP requests and validation.
* Delegates all business logic to the engine service.
* Keeps controllers thin and maintainable.

## CouponEngineService (Engine)

* Central orchestration layer.
* Loads coupons from repository.
* Validates expiration and eligibility.
* Retrieves strategy via factory.
* Aggregates discount responses.

## Strategy Factory

* Maps `CouponType` → Strategy implementation.
* Removes conditional logic from the engine.
* Enables easy extension for new coupon types.

## Strategy Pattern

Each coupon type implements a common interface:

* `isApplicable(cart, coupon)`
* `apply(cart, coupon)`

This isolates business rules and ensures extensibility.

---

#  Core Components

## Coupon Entity

Represents a promotion rule stored in DB.

Key fields:

* id
* type (enum)
* metadata (JSON)
* expirationDate
* active

### Why Metadata JSON?

Different coupon types require different fields.
Storing rules in metadata avoids schema changes when new promotions are introduced.

---

## CouponType Enum

Defines supported coupon categories.

Benefits:

* Compile-time safety
* No magic strings
* Cleaner factory mapping

---

## DTO Layer

### CartRequest

Represents incoming cart payload.

### CartItem

Represents an individual item:

* productId
* quantity
* price

### ApiResponse

Standard response wrapper ensuring consistent API structure.

---

## CouponEngineService Responsibilities

* Fetch coupons
* Filter expired/inactive coupons
* Resolve strategy from factory
* Evaluate eligibility
* Apply discount logic
* Build structured response

The engine coordinates execution but does not contain coupon-specific logic.

---

## Strategy Implementations

### CartWiseCouponStrategy

* Calculates cart total.
* Applies discount if threshold met.
* Ensures safe numeric calculations.

### ProductWiseCouponStrategy

* Applies discount only to eligible products.
* Skips missing products safely.

### BxGyCouponStrategy

* Calculates repetition count.
* Applies free item logic.
* Enforces repetition limits to prevent over-discounting.

---

#  Request Lifecycle

## POST /coupons/applicable-coupons

1. Controller receives cart.
2. Engine fetches all coupons.
3. Expired or inactive coupons filtered.
4. Strategy resolved via factory.
5. Each strategy checks applicability.
6. Discount results aggregated.
7. Response returned without modifying cart.

---

## POST /coupons/apply-coupon/{id}

1. Coupon fetched by ID.
2. Expiration validated.
3. Strategy retrieved via factory.
4. Strategy applies discount to cart.
5. Updated cart returned with totals.

---

#  Data Storage

This project uses **H2 in-memory database**.

Reasons:

* Fast setup for evaluation.
* No infrastructure dependency.
* Focus remains on business logic.

Coupons are reset when the application restarts.

---

# Key Design Decisions

* Thin controllers keep business logic centralized.
* Strategy Pattern allows plug-and-play coupon types.
* Factory Pattern removes conditional branching.
* Metadata JSON enables flexible promotion rules.
* Enum ensures strong typing and safety.

---

# Production-Level Features

* Global exception handling
* Coupon expiration validation
* Structured API responses
* Sample `data.sql` for quick testing
* Strategy-level unit testing approach

---

#  Edge Cases Considered

* Expired coupons
* BXGY repetition limits
* Product not present in cart
* Cart threshold validation
* Unknown coupon strategy handling
* Invalid metadata scenarios

---

#  Assumptions

* Only one coupon applied at a time.
* Prices are non-negative.
* Inventory validation is out of scope.
* Data persistence beyond runtime is not required.

---

#  Limitations

* Coupon stacking rules not implemented.
* Priority engine not included.
* No distributed locking or idempotency.
* No caching layer.

---

#  Future Improvements

If scaled to a large production system:

* Redis caching for coupon eligibility.
* Rule Engine or DSL for dynamic promotions.
* Coupon priority and stacking support.
* Kafka events for coupon usage analytics.
* Distributed idempotency handling.
* External configuration service for promotion rules.

---

# Tech Stack

* Java
* Spring Boot
* H2 In-Memory Database
* JPA / Hibernate
* JUnit

---

#  Running the Project

```
git clone https://github.com/abhishekItgit/monkcommerce-coupon-engine.git
cd monk-commerce-coupon-engine
mvn clean install
mvn spring-boot:run
```

---

#  Summary

The Monk Commerce Coupon Engine demonstrates a scalable backend design using Strategy and Factory patterns to manage complex promotion logic.
The architecture prioritizes extensibility, maintainability, and production-ready design principles while remaining lightweight for assessment purposes.
