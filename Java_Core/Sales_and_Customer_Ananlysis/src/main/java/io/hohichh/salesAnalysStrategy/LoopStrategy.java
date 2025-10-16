package io.hohichh.salesAnalysStrategy;

import io.hohichh.DTO.Customer;
import io.hohichh.DTO.Order;
import io.hohichh.DTO.OrderItem;

import java.util.*;

import static io.hohichh.DTO.OrderStatus.DELIVERED;

public class LoopStrategy implements SalesAnalyzer{
    /**
     * Gets a list of unique cities from all orders using a traditional loop.
     * This method serves as a reference implementation for comparison.
     * @param orders the list of orders to process.
     * @return a list of unique city names.
     */
    @Override
    public List<String> uniqueCityByOrder(List<Order> orders) {
        Set<String> referenceCitiesSet = new HashSet<>();
        for (Order order : orders) {
            referenceCitiesSet.add(order.getCustomer().getCity());
        }
        return new ArrayList<>(referenceCitiesSet);
    }
    /**
     * Calculates the total income from all delivered orders using a traditional loop.
     * This method serves as a reference implementation for comparison.
     * @param orders the list of orders to process.
     * @return the total income as a double.
     */
    @Override
    public double totalIncome(List<Order> orders) {
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
    /**
     * Finds the name of the product with the highest total sales revenue using traditional loops.
     * This method serves as a reference implementation for comparison.
     * @param orders the list of orders to analyze.
     * @return the name of the most popular product, or null if no orders are found.
     */
    @Override
    public String popularProduct(List<Order> orders) {
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
    /**
     * Calculates the average order value for all delivered orders using a traditional loop.
     * This method serves as a reference implementation for comparison.
     * @param orders the list of orders to process.
     * @return the average check value.
     */
    @Override
    public double averageCheck(List<Order> orders) {
        double sum = 0.0;
        int orderCount = 0;
        for (Order order : orders) {
            if (order.getStatus() == DELIVERED) {
                orderCount++;
                for (OrderItem item : order.getItems()) {
                    sum += item.getPrice() * item.getQuantity();
                }
            }
        }
        return sum / orderCount;
    }
    /**
     * Finds all customers who have placed more than 5 orders using a traditional loop.
     * This method serves as a reference implementation for comparison.
     * @param orders the list of orders to analyze.
     * @return a list of customers meeting the criteria.
     */
    @Override
    public List<Customer> moreThenFiveOrderCustomer(List<Order> orders) {
        Map<Customer, List<Order>> customerMap = new HashMap<>();
        for (Order order : orders) {
            customerMap.computeIfAbsent(order.getCustomer(), k -> new ArrayList<>()).add(order);
        }
        customerMap.values().removeIf(customerOrders -> customerOrders.size() <= 5);

        return new ArrayList<>(customerMap.keySet());
    }
}
