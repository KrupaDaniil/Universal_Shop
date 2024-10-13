package com.example.universal_shop.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ErrorController {
    @GetMapping("/errors/fail-registration")
    public String failRegistration(Model model) {
        if (model.containsAttribute("errorRegMessage")) {
            model.addAttribute("errorRegMessage", model.getAttribute("errorRegMessage"));
        };
        return "errors/fail-registration";
    }
}
