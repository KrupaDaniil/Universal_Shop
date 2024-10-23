package com.example.universal_shop.Controllers;

import com.example.universal_shop.Models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/errors/fail-registration")
    public String failRegistration(@AuthenticationPrincipal User user, Model model) {
        String message = "Registration failed. Please try again later.";

        model.addAttribute("user", user);
        model.addAttribute("errorRegMessage", message);

        return "errors/fail-registration";
    }

    @GetMapping("/error/fail-create-order")
    public String failCreateOrder(@AuthenticationPrincipal User user, Model model) {
        String error = "It is not possible to place an order. Please try again.";

        model.addAttribute("user", user);
        model.addAttribute("errorOrderMessage", error);

        return "errors/fail-create-order";
    }
}
