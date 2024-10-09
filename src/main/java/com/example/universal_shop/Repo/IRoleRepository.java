package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByUserRole(String userRole);
}
