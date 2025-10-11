package io.hohichh;

import io.hohichh.DTO.Customer;
import io.hohichh.DTO.Order;
import io.hohichh.DTO.OrderItem;
import io.hohichh.DataSourceMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.hohichh.DTO.OrderStatus.DELIVERED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;

class SalesAndCustomerAnalysisTest {

    private DataSourceMock dataSource;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        dataSource = new DataSourceMock(12345);
        orders = dataSource.generateOrders();
    }

    @Test
    @DisplayName("List of unique cities where orders came from")
    void unique_cities_of_orders_from_test() {
        List<String> actualCities = cities(orders);
        List<String> referenceCities = citiesRef(orders);

        System.out.println("Cities founded with Stream: " + actualCities);
        System.out.println("Cities founded with cycle: " + referenceCities);

        assertThat(actualCities).containsExactlyInAnyOrderElementsOf(referenceCities);
    }

    private List<String> cities(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .distinct()
                .toList();
    }

    private List<String> citiesRef(List<Order> orders) {
        Set<String> referenceCitiesSet = new HashSet<>();
        for (Order order : orders) {
            referenceCitiesSet.add(order.getCustomer().getCity());
        }
        return new ArrayList<>(referenceCitiesSet);
    }


    @Test
    @DisplayName("Total income for all completed orders")
    void total_income_for_all_completed_orders_test() {
        double actualIncome = income(orders);
        double referenceIncome = incomeRef(orders);

        System.out.printf("Income counted with Stream: %.2f\n", actualIncome);
        System.out.printf("Incoume counted with cycle: %.2f\n", referenceIncome);

        assertThat(actualIncome).isEqualTo(referenceIncome, withPrecision(0.01));
    }

    private double income(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private double incomeRef(List<Order> orders) {
        double referenceIncome = 0.0;

        for (Order order : orders) {
            if (order.getStatus() == DELIVERED) {
                for (OrderItem item : order.getItems()) {
                    referenceIncome += item.getPrice() * item.getQuantity();
                }
            }
        }
        return referenceIncome;
    }

    @Test
    @DisplayName("The most popular product by sales")
    void most_popular_product_by_sales_test() {
        String actualProductName = productBySales(orders);
        String referenceProductName = productBySalesRef(orders);

        System.out.println("Most popular product by sales with Stream: " + actualProductName);
        System.out.println("Most popular product by sales with cycles:    " + referenceProductName);

        assertThat(actualProductName).isEqualTo(referenceProductName);
    }

    private String productBySales(List<Order> orders) {
        Map<String, Double> totalRevenueByProduct = orders.stream()
                .filter(order -> order.getStatus() == DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProductName,
                        Collectors.summingDouble(item -> item.getPrice() * item.getQuantity())
                ));

        return totalRevenueByProduct.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private String productBySalesRef(List<Order> orders) {
        Map<String, Double> nameToIncomeRef = new HashMap<>();
        for (Order order : orders) {
            if (order.getStatus() == DELIVERED) {
                for (OrderItem item : order.getItems()) {
                    String productName = item.getProductName();
                    double itemRevenue = item.getPrice() * item.getQuantity();

                    double currentRevenue = nameToIncomeRef.getOrDefault(productName, 0.0);
                    nameToIncomeRef.put(productName, currentRevenue + itemRevenue);
                }
            }
        }
        if (nameToIncomeRef.isEmpty()) {
            return null;
        }

        String mostPopularProduct = null;
        double maxRevenue = -1.0;

        for (Map.Entry<String, Double> entry : nameToIncomeRef.entrySet()) {
            if (entry.getValue() > maxRevenue) {
                maxRevenue = entry.getValue();
                mostPopularProduct = entry.getKey();
            }
        }

        return mostPopularProduct;
    }

    @Test
    @DisplayName("Average check for successfully delivered orders")
    void average_check_for_successfully_delivered_orders_test() {
        double actualAverageCheck = averageCheck(orders);
        double referenceAverageCheck = averageCheckRef(orders);

        System.out.println("Average check for successfully delivered order with Stream: " + actualAverageCheck);
        System.out.println("Average check for successfully delivered order with cycles:    " + referenceAverageCheck);

        assertThat(actualAverageCheck).isEqualTo(referenceAverageCheck);
    }

    private double averageCheck(List<Order> orders){
        List<Double> checkSums = orders.stream()
                .filter(order -> order.getStatus() == DELIVERED)
                .flatMap(order -> Stream.of(order.getItems()))
                .mapToDouble(orderItems ->
                    orderItems.stream()
                            .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum()
                ).boxed().toList();

        return checkSums.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .getAsDouble();
    }

    private double averageCheckRef(List<Order> orders){
        double sum = 0.0;
        int itemCount = 0;
        for (Order order : orders) {
            if (order.getStatus() == DELIVERED) {
                for (OrderItem item : order.getItems()) {
                    sum += item.getPrice() * item.getQuantity();
                    itemCount++;
                }
            }
        }
        return sum / itemCount;
    }

    @Test
    @DisplayName("Customers who have more than 5 orders")
    void customers_who_have_more_than_5_orders_test() {
        List<Customer> actualCustomers = customers(orders);
        List<Customer> referenceCustomers = customersRef(orders);

        System.out.println("Customers who have more than 5 orders with Stream: " + actualCustomers);
        System.out.println("Customers who have more than 5 orders with cycles:    " + referenceCustomers);

        assertThat(actualCustomers).containsExactlyInAnyOrderElementsOf(referenceCustomers);
    }

    private List<Customer> customers(List<Order> orders) {
        Map<Order, Customer> customerMap = new HashMap<>();
        customerMap = orders.stream()

        return null;
    }

    private List<Customer> customersRef(List<Order> orders) {
        return null;
    }
}