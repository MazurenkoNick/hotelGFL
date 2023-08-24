package com.example.hotelgfl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/form-login")
    public String showLoginForm() {
        return "form-login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/form-login?logout";
    }
}
