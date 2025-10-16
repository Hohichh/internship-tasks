package io.hohichh.appcontext.testapp.interfaces;

import io.hohichh.appcontext.testapp.model.Order;

import java.util.List;
import java.util.UUID;

public interface IOrderRepository {
    Order getOrder(UUID id);
    List<Order> getAllOrders();
    void addOrder(Order order);
    void updateOrder(Order order);
    void deleteOrder(UUID id);
}
