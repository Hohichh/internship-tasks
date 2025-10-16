/*
 * Author: Yelizaveta Verkovich aka Hohich
 * Task: Analyze orders list using StreamAPI to collect different business metrics
 * The implementation must be covered with unit tests using JUnit 5.
 */
package io.hohichh;

import io.hohichh.DTO.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;


/**
 * A mock data source for generating random orders, customers, and related data for testing purposes.
 * Uses a seed to ensure that the generated data is reproducible.
 */
public class DataSourceMock {
    private final Faker faker;
    private final Random random;

    /**
     * Constructs a new mock data source with a specific seed.
     * @param seed the seed for the random number generator to ensure reproducibility.
     */
    public DataSourceMock(int seed) {
        this.faker = new Faker(new Random(seed));
        this.random = new Random(seed);
    }

    /**
     * Generates a list of random orders.
     * It first creates a random number of customers and then generates a random number of orders for each customer.
     * @return a list of generated {@link Order} objects.
     */
    public List<Order> generateOrders(){
        int customerAmount = random.nextInt(7) + 1;
        List<Customer> customers = generateCustomers(customerAmount);
        List<Order> orders = new ArrayList<>();

        for(Customer customer : customers){
            int orderAmount = random.nextInt(7) + 1;
            for(int i = 0; i < orderAmount; i++){
                Order order = new OrderGenerator().gen(customer);
                orders.add(order);
            }
        }
        return orders;
    }

    /**
     * Generates a specified number of random customers.
     * @param amount the number of customers to generate.
     * @return a list of generated {@link Customer} objects.
     */
    private List<Customer> generateCustomers(int amount){
        List<Customer> customers = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            Customer customer = new CustomerGenerator().gen();
            customers.add(customer);
        }
        return customers;
    }

    /**
     * An inner helper class for generating {@link Order} objects.
     */
    class OrderGenerator{
        /**
         * Generates a single, complete {@link Order} for a given customer.
         * The order will have a unique ID, a random date, status, and a list of items.
         * @param customer the customer to whom the order belongs.
         * @return a newly generated {@link Order}.
         */
        public Order gen(Customer customer){
            return Order.builder()
                    .orderId(UUID.randomUUID().toString())
                    .orderDate(generateTime())
                    .customer(customer)
                    .items(genOrderItemList(random.nextInt(5)))
                    .status(genStatus())
                    .build();
        }

        /**
         * Generates a list of random order items.
         * @param amount the number of items to generate.
         * @return a list of {@link OrderItem} objects.
         */
        private List<OrderItem> genOrderItemList(int amount){
            List<OrderItem> orderItems = new ArrayList<>();
            for(int i = 0; i < amount; i++){
                orderItems.add(genOrderItem());
            }
            return orderItems;
        }

        /**
         * Generates a single random {@link OrderItem}.
         * @return a new {@link OrderItem} with a random product name, price, category, and quantity.
         */
        private OrderItem genOrderItem(){
            return OrderItem.builder()
                    .productName(faker.beer().name())
                    .price(random.nextDouble(10., 300.) + 1)
                    .category(genCategory())
                    .quantity(random.nextInt(100) + 1 )
                    .build();
        }

        /**
         * Selects a random {@link OrderStatus}.
         * @return a random value from the {@link OrderStatus} enum.
         */
        private OrderStatus genStatus(){
            OrderStatus[] statuses = OrderStatus.values();
            return statuses[random.nextInt(statuses.length)];
        }

        /**
         * Selects a random product {@link Category}.
         * @return a random value from the {@link Category} enum.
         */
        private Category genCategory(){
            Category[] categories = Category.values();
            return categories[random.nextInt(categories.length)];
        }
    }

    /**
     * An inner helper class for generating {@link Customer} objects.
     */
    class CustomerGenerator{
        /**
         * Generates a single random {@link Customer}.
         * The customer will have a unique ID and fake personal details.
         * @return a newly generated {@link Customer}.
         */
        public Customer gen(){
            return Customer
                    .builder()
                    .customerId(UUID.randomUUID().toString())
                    .name(faker.name().firstName())
                    .age(random.nextInt(100))
                    .email(faker.internet().emailAddress())
                    .city(faker.address().city())
                    .registeredAt(generateTime()).build();
        }

    }

    /**
     * Generates a random {@link LocalDateTime} within the last 5 years.
     * @return a random date and time.
     */
    private LocalDateTime generateTime(){
        Date pastDate = faker.date().past(365 * 5, TimeUnit.DAYS);
        return pastDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
