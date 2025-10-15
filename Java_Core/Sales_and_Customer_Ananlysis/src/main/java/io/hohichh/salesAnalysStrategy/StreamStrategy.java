package io.hohichh.salesAnalysStrategy;

import io.hohichh.DTO.Customer;
import io.hohichh.DTO.Order;
import io.hohichh.DTO.OrderItem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.hohichh.DTO.OrderStatus.DELIVERED;

public class StreamStrategy implements SalesAnalyzer{
    /**
     * Gets a list of unique cities from all orders using the Stream API.
     * @param orders the list of orders to process.
     * @return a list of unique city names.
     */
    @Override
    public List<String> uniqueCityByOrder(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .distinct()
                .toList();
    }
    /**
     * Calculates the total income from all delivered orders using the Stream API.
     * @param orders the list of orders to process.
     * @return the total income as a double.
     */
    @Override
    public double totalIncome(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
    /**
     * Finds the name of the product with the highest total sales revenue using the Stream API.
     * It only considers orders that have been delivered.
     * @param orders the list of orders to analyze.
     * @return the name of the most popular product, or null if no orders are found.
     */
    @Override
    public String popularProduct(List<Order> orders) {
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
    /**
     * Calculates the average order value for all delivered orders using the Stream API.
     * @param orders the list of orders to process.
     * @return the average check value.
     */
    @Override
    public double averageCheck(List<Order> orders) {
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
    /**
     * Finds all customers who have placed more than 5 orders using the Stream API.
     * @param orders the list of orders to analyze.
     * @return a list of customers meeting the criteria.
     */
    @Override
    public List<Customer> moreThenFiveOrderCustomer(List<Order> orders) {
        Map<Customer, List<Order>> customerMap = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getCustomer
                ));
        return customerMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 5)
                .map(Map.Entry::getKey)
                .toList();
    }
}
