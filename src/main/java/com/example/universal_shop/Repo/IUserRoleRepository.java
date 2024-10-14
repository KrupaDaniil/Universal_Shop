package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface IUserRoleRepository extends JpaRepository<UserRole, Long> {
    Set<UserRole> findByUser_Id(long id);
    Set<UserRole> findByRole_Id(long id);
    boolean existsByUser_IdAndRole_Id(long userId, long roleId);
    boolean existsById(long id);
}
