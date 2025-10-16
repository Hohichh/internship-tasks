package io.hohichh.salesAnalysStrategy;

import io.hohichh.DTO.Customer;
import io.hohichh.DTO.Order;

import java.util.List;

public interface SalesAnalyzer {
    List<String> uniqueCityByOrder(List<Order> orders);
    double totalIncome(List<Order> orders);
    String popularProduct(List<Order> orders);
    double averageCheck(List<Order> orders);
    List<Customer> moreThenFiveOrderCustomer(List<Order> orders);
}
