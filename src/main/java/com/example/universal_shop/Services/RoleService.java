package com.example.universal_shop.Services;

import com.example.universal_shop.Models.Role;
import com.example.universal_shop.Repo.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final IRoleRepository roleRepository;

    @Autowired
    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

    public Role findByName(String name) {
        return roleRepository.findByUserRole(name).orElse(null);
    }

    public Role findById(long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public void delete(long id) {
        roleRepository.deleteById(id);
    }

    public boolean existsByRole(String role) {
        return roleRepository.existsByUserRole(role);
    }

}
