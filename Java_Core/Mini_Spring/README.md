# MiniSpring Project Report

## Task

The primary goal of this project was to develop a lightweight, simplified version of the Spring Framework's Inversion of Control (IoC) container, named "MiniSpring". The objective was to gain a practical understanding of the core principles behind Dependency Injection (DI) and IoC.

The container was required to:
1.  Scan a specified package for classes annotated with a custom `@Component` annotation.
2.  Instantiate these classes as "beans" and manage their lifecycle.
3.  Automatically inject dependencies into fields marked with a custom `@Autowired` annotation.
4.  Support the `InitializingBean` interface for post-initialization logic.
5.  Implement two standard bean scopes: `singleton` (default) and `prototype`.

## Implementation Details

The implementation revolves around the central `MiniApplicationContext` class, which orchestrates the entire process using the Java Reflection API.

### 1. IoC Container (`MiniApplicationContext`)
The context is initialized in its constructor, performing a three-stage startup process:
*   **Scanning:** It scans the provided package for component classes. The scanner is robust, capable of discovering classes from both the local file system (during development in an IDE) and from within JAR files (in a production environment).
*   **Instantiation:** It identifies components and separates them into two categories based on their `@Scope` annotation. Singleton beans are instantiated immediately and stored in a cache (`Map<Class<?>, Object>`). Prototype beans are not instantiated at this stage; only their class definitions are stored.
*   **Injection & Initialization:** The context iterates through all newly created singleton beans. For each bean, it inspects its fields, finds those annotated with `@Autowired`, and injects the required dependency by retrieving it from the container. Finally, it calls the `afterPropertiesSet()` method on any bean that implements the `InitializingBean` interface.

### 2. Custom Annotations
*   **`@Component`**: A class-level annotation that marks a class as a candidate for bean creation.
*   **`@Autowired`**: A field-level annotation that marks a dependency to be injected by the container.
*   **`@Scope`**: A class-level annotation that defines the lifecycle of a bean. It accepts a string value, with `"singleton"` being the default and `"prototype"` creating a new instance on each request.

### 3. Bean Scopes
The container manages two distinct scopes:
*   **Singleton:** Only one instance of the bean is created and shared throughout the application's lifecycle. This is the default behavior.
*   **Prototype:** A new instance of the bean is created, configured, and returned every time it is requested from the context via the `getBean()` method. This is achieved by creating and dependency-injecting the object on-the-fly within the `getBean()` call itself.

## Test Scenario

To verify the correct functionality of the container, particularly the bean scopes, a clear and direct test was conducted within the `main` application entry point. This approach avoids unnecessary complexity and directly validates the container's behavior.

### 1. Singleton Scope Test
*   **Action:** The `IUserService` bean (a singleton by default) was requested from the context twice, storing the results in two different variables (`userService1` and `userService2`).
*   **Verification:** An identity check (`userService1 == userService2`) was performed.
*   **Expected Result:** The check should evaluate to `true`, proving that the container returned a reference to the exact same object instance for both requests.

### 2. Prototype Scope Test
*   **Action:** The `ReportGenerator` bean, explicitly marked with `@Scope("prototype")`, was requested from the context twice (`report1` and `report2`).
*   **Verification:** An identity check (`report1 != report2`) was performed.
*   **Expected Result:** The check should evaluate to `true`, demonstrating that the container created two separate and independent object instances. To further confirm this, the state of each instance was modified independently, showing that changes in one did not affect the other.

Both tests passed successfully, confirming that the `MiniApplicationContext` correctly implements and manages the `singleton` and `prototype` bean scopes as intended.

Results:

```angular2html
OrderService constructor called. Repository is still null here.
UserService constructor called. Repository is still null here.

OrderService.afterPropertiesSet() called. Repository is now injected.
UserService.afterPropertiesSet() called. Repository is now injected.

--- Starting Application Scenario ---
Created User: Alice with ID: f4d86b36-6336-4106-b8e1-1db30c3a8134
Created Order: Laptop with ID: b399ff08-64bf-4074-ad4d-0473b532dd2d

--- Current Users in Repository ---
User ID: f4d86b36-6336-4106-b8e1-1db30c3a8134, Name: Alice

--- Application Scenario Finished ---
---Test scope: Singleton ---
1st IUserService: io.hohichh.appcontext.testapp.UserService@1175e2db
2nd IUserService: io.hohichh.appcontext.testapp.UserService@1175e2db
>>> Success! Same reference to object!

--- Test scope: Prototype ---
>>> New ReportGenerator instance created!
>>> New ReportGenerator instance created!
1st ReportGenerator: io.hohichh.appcontext.testapp.ReportGenerator@36aa7bc2
2nd ReportGenerator: io.hohichh.appcontext.testapp.ReportGenerator@76ccd017
>>> Success! Different reference to object!

Reports:
=========================================
REPORT: Report 1
-----------------------------------------
=========================================

=========================================
REPORT: Diff report 2
-----------------------------------------
=========================================

```
