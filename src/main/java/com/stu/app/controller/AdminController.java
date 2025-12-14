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

    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SchoolClassRepository classRepository; // Thêm cái này để quản lý lớp

    // 1. Trả về View HTML
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    // --- CÁC API XỬ LÝ LỚP HỌC (Được gộp vào đây thay vì tạo file mới) ---

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
        // Gọi Repository tìm học sinh trong lớp
        List<Student> students = studentRepository.findBySchoolClass_ClassId(id);

        // Chuyển đổi sang UserDTO để Frontend hiển thị
        List<UserDTO> result = new ArrayList<>();
        for (Student s : students) {
            UserDTO dto = new UserDTO();

            // Lấy User ID nếu đã liên kết tài khoản
            if (s.getUser() != null) {
                dto.setUserId(s.getUser().getUserId());
                dto.setUsername(s.getUser().getUsername());
            } else {
                dto.setUsername(s.getStudentId()); // Fallback nếu chưa có user
            }

            dto.setFullName(s.getFullName());
            dto.setGender(s.getGender());
            dto.setBirth(s.getBirth());

            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }
}