package com.example.universal_shop.Services;

import com.example.universal_shop.Enum.UserRoles;
import com.example.universal_shop.Models.Role;
import com.example.universal_shop.Models.User;
import com.example.universal_shop.Models.UserRole;
import com.example.universal_shop.Repo.IRoleRepository;
import com.example.universal_shop.Repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Autowired
    public UserService(IUserRepository userRepository, IRoleRepository roleRepository)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }

        return user;
    }

    public void registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        Role role = roleRepository.findByUserRole(UserRoles.ROLE_USER.toString()).orElse(null);

        user.setEnabled(true);
        user.setLocked(false);
        if (role != null) {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);

            user.setUserRoles(Set.of(userRole));
        }

        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void saveUser(User user) {

        userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
