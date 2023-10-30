package ru.kata.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.springboot.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("UPDATE User u SET u.name = :#{#user.name}, u.email = :#{#user.email}, u.age = :#{#user.age} WHERE u.id = :#{#user.id}")
    void updateUserById(@Param("user") User user);
}
