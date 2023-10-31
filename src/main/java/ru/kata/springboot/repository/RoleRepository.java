package ru.kata.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.springboot.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
