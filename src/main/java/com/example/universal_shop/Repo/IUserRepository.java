package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
