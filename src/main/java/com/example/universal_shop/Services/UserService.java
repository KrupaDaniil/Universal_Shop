package com.example.universal_shop.Services;

import com.example.universal_shop.Enum.Roles;
import com.example.universal_shop.Models.Role;
import com.example.universal_shop.Models.User;
import com.example.universal_shop.Models.UserRole;
import com.example.universal_shop.Repo.IRoleRepository;
import com.example.universal_shop.Repo.IUserRepository;
import com.example.universal_shop.Repo.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;
    private final IUserRoleRepository userRoleRepository;

    @Autowired
    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder,
                       IRoleRepository roleRepository, IUserRoleRepository userRoleRepository)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }

        user.setUserRoles(userRoleRepository.findByUser_Id(user.getId()));

        return user;
    }

    public void registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        Role role = roleRepository.findByUserRole(Roles.USER.toString()).orElse(null);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public long findByRoleId(long id) {
        Set<UserRole> userRole = userRoleRepository.findByUser_Id(id);

        long roleId = 0;

        if (userRole == null) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }

        for (UserRole ur : userRole) {
            roleId = ur.getRole().getId();
        }

        return roleId;
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
