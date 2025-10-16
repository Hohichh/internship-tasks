package io.hohichh.appcontext.testapp;

import io.hohichh.appcontext.annotations.Component;
import io.hohichh.appcontext.testapp.interfaces.IUserRepository;
import io.hohichh.appcontext.testapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepository implements IUserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public User getUser(UUID id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public void addUser(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        users.add(user);
    }

    @Override
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                return;
            }
        }
    }

    @Override
    public void deleteUser(UUID id) {
        users.removeIf(u -> u.getId().equals(id));
    }
}