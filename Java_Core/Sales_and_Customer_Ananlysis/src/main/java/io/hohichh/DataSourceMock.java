package io.hohichh;

import io.hohichh.DTO.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;


public class DataSourceMock {
    private final Faker faker;
    private final Random random;

    public DataSourceMock(int seed) {
        this.faker = new Faker(new Random(seed));
        this.random = new Random(seed);
    }

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

    private List<Customer> generateCustomers(int amount){
        List<Customer> customers = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            Customer customer = new CustomerGenerator().gen();
            customers.add(customer);
        }
        return customers;
    }

    class OrderGenerator{
        public Order gen(Customer customer){
            return Order.builder()
                    .orderId(UUID.randomUUID().toString())
                    .orderDate(generateTime())
                    .customer(customer)
                    .items(genOrderItemList(random.nextInt(5)))
                    .status(genStatus())
                    .build();
        }

        private List<OrderItem> genOrderItemList(int amount){
            List<OrderItem> orderItems = new ArrayList<>();
            for(int i = 0; i < amount; i++){
                orderItems.add(genOrderItem());
            }
            return orderItems;
        }

        private OrderItem genOrderItem(){
            return OrderItem.builder()
                    .productName(faker.beer().name())
                    .price(random.nextDouble(10., 300.) + 1)
                    .category(genCategory())
                    .quantity(random.nextInt(100) + 1 )
                    .build();
        }

        private OrderStatus genStatus(){
            OrderStatus[] statuses = OrderStatus.values();
            return statuses[random.nextInt(statuses.length)];
        }

        private Category genCategory(){
            Category[] categories = Category.values();
            return categories[random.nextInt(categories.length)];
        }
    }

    class CustomerGenerator{
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

    private LocalDateTime generateTime(){
        Date pastDate = faker.date().past(365 * 5, TimeUnit.DAYS);
        return pastDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
