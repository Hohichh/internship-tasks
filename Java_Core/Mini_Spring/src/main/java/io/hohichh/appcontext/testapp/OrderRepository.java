package io.hohichh.appcontext.testapp;

import io.hohichh.appcontext.annotations.Component;
import io.hohichh.appcontext.testapp.interfaces.IOrderRepository;
import io.hohichh.appcontext.testapp.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class OrderRepository implements IOrderRepository {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public Order getOrder(UUID id) {
        return orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public void addOrder(Order order) {
        if (order.getId() == null) {
            order.setId(UUID.randomUUID());
        }
        orders.add(order);
    }

    @Override
    public void updateOrder(Order order) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(order.getId())) {
                orders.set(i, order);
                return;
            }
        }
    }

    @Override
    public void deleteOrder(UUID id) {
        orders.removeIf(o -> o.getId().equals(id));
    }
}