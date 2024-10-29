package com.example.universal_shop.Controllers;

import com.example.universal_shop.Models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginShowPage(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/";
        }

        return "login";
    }
}
