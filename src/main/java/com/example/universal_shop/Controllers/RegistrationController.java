package com.example.universal_shop.Controllers;

import com.example.universal_shop.Models.DTOs.UserDTO;
import com.example.universal_shop.Models.User;
import com.example.universal_shop.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDTO user, RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(new User(user.getName(), user.getSurname(), user.getEmail(),
                    user.getPassword(), user.getPhone()));
            return "redirect:/login";
        }
        catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorRegMessage", ex.getMessage());
            return "redirect:errors/fail-registration";
        }

    }
}
