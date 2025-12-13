package com.stu.app.controller;

import com.stu.app.model.Role;
import com.stu.app.model.SchoolClass;
import com.stu.app.model.Student;
import com.stu.app.model.User;
import com.stu.app.repository.RoleRepository;
import com.stu.app.repository.StudentRepository;
import com.stu.app.repository.UserRepository;
import com.stu.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Cần thêm các Repository này để lưu dữ liệu trực tiếp tại Controller
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository; // Dùng để tìm Role từ DB

    // 1. Trả về View HTML
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    // 2. Các API JSON

    @ResponseBody
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    // --- API TẠO USER MỚI (Logic bạn cần ở đây) ---
    @ResponseBody
    @PostMapping("/api/users")
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> payload) {
        try {
            // --- BƯỚC 1: XỬ LÝ USER (TÀI KHOẢN) ---
            User user = new User();
            user.setUsername((String) payload.get("username"));
            // Lưu ý: Password nên được mã hóa (BCrypt) trong thực tế
            user.setPassword((String) payload.get("password"));

            // Xử lý Role
            Map<String, String> roleMap = (Map<String, String>) payload.get("role");
            String roleName = roleMap.get("roleName");

            // Tìm Role trong DB (hoặc tạo mới object Role nếu lười query)
            Role role = roleRepository.findByRoleName(roleName);
            if (role == null) {
                role = new Role();
                role.setRoleName(roleName); // Chỉ dùng tạm nếu DB chưa có role chuẩn
            }
            user.setRole(role);

            // Lưu User vào DB trước
            User savedUser = userRepository.save(user);

            // --- BƯỚC 2: NẾU LÀ STUDENT -> XỬ LÝ TIẾP ---
            if ("STUDENT".equals(roleName)) {
                Student student = new Student();

                // Lấy thông tin cá nhân từ payload
                student.setFullName((String) payload.get("fullName"));
                student.setGender((String) payload.get("gender"));
                String birthStr = (String) payload.get("birth");
                if (birthStr != null && !birthStr.isEmpty()) {
                    student.setBirth(LocalDate.parse(birthStr));
                }

                // ... set class ...
                Map<String, Object> classData = (Map<String, Object>) payload.get("SchoolClass");
                if (classData != null && classData.get("classId") != null) {
                    Long classId = Long.valueOf(classData.get("classId").toString());

                    SchoolClass schoolClass = new SchoolClass();
                    schoolClass.setClassId(classId);

                    // --- SỬA TẠI ĐÂY ---
                    student.setSchoolClass(schoolClass); // Dùng tên hàm mới
                    // -------------------
                }

                // --- QUAN TRỌNG: GÁN USER VỪA TẠO VÀO STUDENT ---
                student.setUser(savedUser);
                // ------------------------------------------------

                studentRepository.save(student);
            }

            return ResponseEntity.ok(savedUser);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // Lưu ý: Nếu user là Student, cần xoá Student trước (hoặc dùng Cascade)
        adminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}