package com.stu.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    // Chỉ giữ lại cái này để hiển thị form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login"; // Hoặc trả về trang chủ công khai nếu có
    }

    // Không cần hàm logout ở đây vì SecurityConfig đã có .logoutUrl("/logout")
}