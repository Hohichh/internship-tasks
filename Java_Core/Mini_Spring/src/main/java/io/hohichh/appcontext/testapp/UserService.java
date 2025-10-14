package io.hohichh.appcontext.testapp;

import io.hohichh.appcontext.InitializingBean;
import io.hohichh.appcontext.annotations.Autowired;
import io.hohichh.appcontext.annotations.Component;
import io.hohichh.appcontext.testapp.interfaces.IUserRepository;
import io.hohichh.appcontext.testapp.interfaces.IUserService;
import io.hohichh.appcontext.testapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserService implements IUserService, InitializingBean {
    @Autowired
    private IUserRepository repository;

    public UserService() {
        System.out.println("UserService constructor called. Repository is still null here.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (repository == null) {
            throw new IllegalStateException("User repository has not been injected.");
        }
        System.out.println("UserService.afterPropertiesSet() called. Repository is now injected.");
    }

    @Override
    public User getUser(UUID id) {
        User user = repository.getUser(id);
        if (user == null) {
            user = new User();
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = repository.getAllUsers();
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    @Override
    public void addUser(User user) {
        try {
            repository.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            repository.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(UUID id) {
        try {
            repository.deleteUser(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}