package io.hohichh.appcontext.testapp.interfaces;

import io.hohichh.appcontext.testapp.model.User;

import java.util.List;
import java.util.UUID;

public interface IUserRepository {
    User getUser(UUID id);
    List<User> getAllUsers();
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(UUID id);
}
