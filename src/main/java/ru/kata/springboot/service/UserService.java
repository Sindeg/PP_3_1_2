package ru.kata.springboot.service;

import ru.kata.springboot.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    void updateUser(User user);

    void deleteById(Long id);

    Optional<User> findByEmail(String email);
}
