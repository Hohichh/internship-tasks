package io.hohichh.appcontext.testapp;

import io.hohichh.appcontext.InitializingBean;
import io.hohichh.appcontext.annotations.Autowired;
import io.hohichh.appcontext.annotations.Component;
import io.hohichh.appcontext.testapp.interfaces.IOrderRepository;
import io.hohichh.appcontext.testapp.interfaces.IOrderService;
import io.hohichh.appcontext.testapp.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class OrderService implements IOrderService, InitializingBean {
    @Autowired
    private IOrderRepository orderRepository;

    public OrderService() {
        System.out.println("OrderService constructor called. Repository is still null here.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("OrderService.afterPropertiesSet() called. Repository is now injected.");
        if (orderRepository == null) {
            throw new IllegalStateException("Dependency IOrderRepository has not been injected.");
        }
    }

    @Override
    public Order getOrder(UUID id) {
        Order order = orderRepository.getOrder(id);
        if (order == null) {
            return new Order();
        }
        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.getAllOrders();
        if (orders == null) {
            return new ArrayList<>();
        }
        return orders;
    }

    @Override
    public void addOrder(Order order) {
        try {
            orderRepository.addOrder(order);
        } catch (Exception e) {
            System.err.println("Error adding order: " + order.getId());
            e.printStackTrace();
        }
    }

    @Override
    public void updateOrder(Order order) {
        try {
            orderRepository.updateOrder(order);
        } catch (Exception e) {
            System.err.println("Error updating order: " + order.getId());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrder(UUID id) {
        try {
            orderRepository.deleteOrder(id);
        } catch (Exception e) {
            System.err.println("Error deleting order: " + id);
            e.printStackTrace();
        }
    }
}