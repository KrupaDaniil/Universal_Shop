package com.example.universal_shop.Services;

import com.example.universal_shop.Models.DTOs.UserDTO;
import com.example.universal_shop.Models.User;
import com.example.universal_shop.Repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private IUserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());

        if (user == null) {
            throw new IllegalArgumentException("User with email " + userDTO.getEmail() + " not found");
        }
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setLocked(false);
        userRepository.save(user);
    }
}
