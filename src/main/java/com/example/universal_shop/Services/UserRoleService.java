package com.example.universal_shop.Services;

import com.example.universal_shop.Models.UserRole;
import com.example.universal_shop.Repo.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserRoleService {
    IUserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleService(IUserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }


    public void save(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    public Set<UserRole> findByUser_Id(long userId) {
        return userRoleRepository.findByUser_Id(userId);
    }

    public Set<UserRole> findByRole_Id(long roleId) {
        return userRoleRepository.findByRole_Id(roleId);
    }

    public boolean findByUser_IdAndRole_Id(long userId, long roleId) {
        return userRoleRepository.existsByUser_IdAndRole_Id(userId, roleId);
    }

    public boolean existsById(long id) {
        return userRoleRepository.existsById(id);
    }

    @Transactional
    public void deleteById(long id) {
        userRoleRepository.deleteById(id);
    }
}
