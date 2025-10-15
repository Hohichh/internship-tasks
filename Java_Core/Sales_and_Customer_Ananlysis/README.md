# Sales and Customer Analysis

## Task Description

The objective of this project is to implement the computation of several key business metrics using Java's Stream API. The analysis is performed on a given set of data structures representing e-commerce orders.

The required business metrics are:
- A list of unique cities from which orders were placed.
- The total income generated from all successfully completed orders.
- The most popular product, determined by total sales revenue.
- The average order value (average check) for all successfully delivered orders.
- A list of all customers who have placed more than five orders.

## Implementation Details

### Data Model

The core data model is defined within the `io.hohichh.DTO` package. It consists of the following main data structures:

-   `Order`: Represents a single customer order.
-   `OrderItem`: Represents a single item within an order.
-   `Customer`: Represents a customer.
-   `OrderStatus`: An enum for the status of an order (e.g., `DELIVERED`, `PROCESSING`).
-   `Category`: An enum for the product category.

All DTO (Data Transfer Object) classes are implemented using Project Lombok's `@Data` annotation to reduce boilerplate code and the `@Builder` pattern for easy object creation.

### Data Generation

To facilitate testing and demonstration, a mock data source was created in the `DataSourceMock` class. This class uses the **Java Faker** library to generate a realistic and reproducible list of `Order` objects. The data includes randomly generated customers, order items, and order details, with the randomization being controlled by a seed for consistent test runs.

### Business Logic

The business logic for calculating metrics has been structured using the **Strategy design pattern** to ensure a clear separation of concerns and flexibility. This approach avoids monolithic classes and improves maintainability.

A central interface, `SalesAnalyzer`, defines the contract for any analysis implementation. Two concrete strategies are provided, each encapsulating a different algorithmic approach:
1.  **`StreamStrategy`**: Implements the calculations using the modern, declarative **Stream API**.
2.  **`LoopStrategy`**: Provides a reference implementation using traditional loops and imperative logic.

This design decouples the client code (in this case, the tests) from the specific implementation details, making the system easier to maintain and extend with new analysis strategies in the future.

## Test Coverage

The project's correctness is ensured through a comprehensive set of unit tests located in `SalesAndCustomerAnalysisTest`. Each business metric is covered by a dedicated test method that validates the logic by comparing the outputs of both strategies.

The testing strategy is as follows:
1.  For each metric, an instance of the `StreamStrategy` and the `LoopStrategy` are executed with the same input data.
2.  The results from both methods are then compared using the **AssertJ** assertion library.
3.  Tests are considered successful only if the output from the `StreamStrategy` version exactly matches the output from the reference `LoopStrategy` version, thus validating the correctness of the stream-based data processing logic.