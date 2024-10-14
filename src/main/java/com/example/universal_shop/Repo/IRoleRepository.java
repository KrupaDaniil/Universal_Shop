package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByUserRole(String userRole);
    boolean existsByUserRole(String userRole);
    boolean existsById(long id);
}
