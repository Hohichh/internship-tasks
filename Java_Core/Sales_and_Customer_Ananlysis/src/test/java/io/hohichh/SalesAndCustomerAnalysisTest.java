/*
 * Author: Yelizaveta Verkovich aka Hohich
 * Task: Analyze orders list using StreamAPI to collect different business metrics
 * The implementation must be covered with unit tests using JUnit 5.
 */

package io.hohichh;

import io.hohichh.DTO.Customer;
import io.hohichh.DTO.Order;
import io.hohichh.DTO.OrderItem;
import io.hohichh.salesAnalysStrategy.LoopStrategy;
import io.hohichh.salesAnalysStrategy.SalesAnalyzer;
import io.hohichh.salesAnalysStrategy.StreamStrategy;
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
    private SalesAnalyzer actual;
    private SalesAnalyzer reference;

    @BeforeEach
    void setUp() {
        dataSource = new DataSourceMock(12345);
        orders = dataSource.generateOrders();
        actual = new StreamStrategy();
        reference = new LoopStrategy();
    }

    @Test
    @DisplayName("List of unique cities where orders came from")
    void unique_cities_of_orders_from_test() {
        List<String> actualCities = actual.uniqueCityByOrder(orders);
        List<String> referenceCities = reference.uniqueCityByOrder(orders);

        System.out.println("Cities founded with Stream: " + actualCities);
        System.out.println("Cities founded with cycle: " + referenceCities);

        assertThat(actualCities).containsExactlyInAnyOrderElementsOf(referenceCities);
    }


    @Test
    @DisplayName("Total income for all completed orders")
    void total_income_for_all_completed_orders_test() {
        double actualIncome = actual.totalIncome(orders);
        double referenceIncome = reference.totalIncome(orders);

        System.out.printf("Income counted with Stream: %.2f\n", actualIncome);
        System.out.printf("Incoume counted with cycle: %.2f\n", referenceIncome);

        assertThat(actualIncome).isEqualTo(referenceIncome, withPrecision(0.01));
    }



    @Test
    @DisplayName("The most popular product by sales")
    void most_popular_product_by_sales_test() {
        String actualProductName = actual.popularProduct(orders);
        String referenceProductName = reference.popularProduct(orders);

        System.out.println("Most popular product by sales with Stream: " + actualProductName);
        System.out.println("Most popular product by sales with cycles:    " + referenceProductName);

        assertThat(actualProductName).isEqualTo(referenceProductName);
    }


    @Test
    @DisplayName("Average check for successfully delivered orders")
    void average_check_for_successfully_delivered_orders_test() {
        double actualAverageCheck = Math.round(
                actual.averageCheck(orders)
                        * 100.0) / 100.0;
        double referenceAverageCheck = Math.round(
                reference.averageCheck(orders)
                        * 100.0) / 100.0;

        System.out.println("Average check for successfully delivered order with Stream: " + actualAverageCheck);
        System.out.println("Average check for successfully delivered order with cycles:    " + referenceAverageCheck);

        assertThat(actualAverageCheck).isEqualTo(referenceAverageCheck);
    }



    @Test
    @DisplayName("Customers who have more than 5 orders")
    void customers_who_have_more_than_5_orders_test() {
        List<Customer> actualCustomers = actual.moreThenFiveOrderCustomer(orders);
        List<Customer> referenceCustomers = reference.moreThenFiveOrderCustomer(orders);

        System.out.println("Customers who have more than 5 orders with Stream: " + actualCustomers);
        System.out.println("Customers who have more than 5 orders with cycles:    " + referenceCustomers);

        assertThat(actualCustomers).containsExactlyInAnyOrderElementsOf(referenceCustomers);
    }

}