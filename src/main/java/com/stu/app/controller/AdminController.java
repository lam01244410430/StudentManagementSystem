package com.stu.app.controller;

import com.stu.app.model.SchoolClass;
import com.stu.app.model.User;
import com.stu.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // Dùng @Controller cho view, bên dưới dùng @ResponseBody cho API
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 1. Trả về View HTML
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard"; // templates/admin/dashboard.html
    }

    // 2. Các API JSON (Gọi từ fetch JS)

    @ResponseBody
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @ResponseBody
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @GetMapping("/api/classes")
    public List<SchoolClass> getAllClasses() {
        return adminService.getAllClasses();
    }
}