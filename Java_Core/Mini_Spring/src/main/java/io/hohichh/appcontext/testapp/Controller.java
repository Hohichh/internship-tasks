package io.hohichh.appcontext.testapp;

import io.hohichh.appcontext.annotations.Autowired;
import io.hohichh.appcontext.annotations.Component;
import io.hohichh.appcontext.testapp.interfaces.IOrderService;
import io.hohichh.appcontext.testapp.interfaces.IUserService;
import io.hohichh.appcontext.testapp.model.Order;
import io.hohichh.appcontext.testapp.model.User;

import java.util.UUID;

@Component
public class Controller {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    public Controller() {
    }

    public void runApplicationScenario() {
        System.out.println("--- Starting Application Scenario ---");

        User newUser = new User();
        newUser.setName("Alice");
        newUser.setId(UUID.randomUUID());
        newUser.setEmail("alice@gmail.com");
        userService.addUser(newUser);
        System.out.println("Created User: " + newUser.getName() + " with ID: " + newUser.getId());

        UUID aliceId = newUser.getId();

        Order newOrder = new Order();
        newOrder.setUserId(aliceId);
        newOrder.setId(UUID.randomUUID());
        newOrder.setProduct("Laptop");
        newOrder.setDescription("bla-bla-bla");

        orderService.addOrder(newOrder);
        System.out.println("Created Order: " + newOrder.getProduct() + " with ID: " + newOrder.getId());


        System.out.println("\n--- Current Users in Repository ---");
        userService.getAllUsers().forEach(user ->
                System.out.println("User ID: " + user.getId() + ", Name: " + user.getName())
        );

        System.out.println("\n--- Application Scenario Finished ---");
    }
}