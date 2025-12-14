package com.stu.app.controller;

import com.stu.app.dto.UserDTO;
import com.stu.app.model.SchoolClass;
import com.stu.app.model.Student;
import com.stu.app.repository.SchoolClassRepository;
import com.stu.app.repository.StudentRepository;
import com.stu.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    // Giữ nguyên các Autowired
    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SchoolClassRepository classRepository;

    // API: Trả về View HTML
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    // API: Lấy danh sách lớp
    @ResponseBody
    @GetMapping("/api/classes")
    public List<SchoolClass> getAllClasses() {
        return classRepository.findAll();
    }

    // API: Tạo lớp mới
    @ResponseBody
    @PostMapping("/api/classes")
    public ResponseEntity<SchoolClass> createClass(@RequestBody SchoolClass schoolClass) {
        return ResponseEntity.ok(classRepository.save(schoolClass));
    }

    // API: Xóa lớp
    @ResponseBody
    @DeleteMapping("/api/classes/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        try {
            classRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting class. It might contain students.");
        }
    }

    // API: Lấy danh sách học sinh theo lớp (View Students)
    @ResponseBody
    @GetMapping("/api/classes/{id}/students")
    public ResponseEntity<List<UserDTO>> getStudentsByClass(@PathVariable Long id) {
        // 1. Gọi hàm tìm kiếm trong Repository
        List<Student> students = studentRepository.findBySchoolClass_ClassId(id);

        // 2. Chuyển đổi sang UserDTO
        List<UserDTO> result = new ArrayList<>();
        for (Student s : students) {
            UserDTO dto = new UserDTO();

            // Student ID (Cột đầu tiên trong bảng Frontend): Sử dụng studentId (là String)
            // hoặc userId (Long) nếu có liên kết User
            if (s.getUser() != null) {
                dto.setUserId(s.getUser().getUserId()); // Dùng cho cột ID trong bảng
                dto.setUsername(s.getUser().getUsername());
            } else {
                // Fallback: Nếu không có User, dùng studentId (là ID trong bảng student)
                dto.setUsername(s.getStudentId());
            }

            // Map các trường thông tin còn lại
            dto.setFullName(s.getFullName()); // Đã sửa: Dùng getFullName()
            dto.setGender(s.getGender());
            dto.setBirth(s.getBirth());

            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }

}