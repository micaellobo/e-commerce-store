package com.example.authservice.repository;

import com.example.authservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u) > 0 FROM users u WHERE u.username = :#{#user.username} OR u.email = :#{#user.email}")
    boolean existsUser(@Param("user") User user);

    Optional<User> findUserByUsername(String username);
}