package com.stu.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Trả về templates/login.html
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login"; // Mặc định vào là bắt login
    }
}