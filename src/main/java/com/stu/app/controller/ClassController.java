package com.stu.app.controller;

import com.stu.app.model.SchoolClass;
import com.stu.app.model.Student; // Sửa import thành Student
import com.stu.app.repository.SchoolClassRepository;
import com.stu.app.repository.StudentRepository; // Import repository mới
import com.stu.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClassController {

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Autowired
    private StudentRepository studentRepository; // [QUAN TRỌNG] Dùng StudentRepository

    @Autowired
    private UserRepository userRepository; // [QUAN TRỌNG] Dùng StudentRepository
    // --- API ---

    @GetMapping("/api/classes")
    @ResponseBody
    public List<SchoolClass> getAllClasses() {
        return schoolClassRepository.findAll();
    }

    @PostMapping("/api/classes")
    @ResponseBody
    public SchoolClass createClass(@RequestBody SchoolClass newClass) {
        return schoolClassRepository.save(newClass);
    }

    @DeleteMapping("/api/classes/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        schoolClassRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // [QUAN TRỌNG] API lấy danh sách học sinh trong lớp
    @GetMapping("/api/classes/{classId}/students")
    @ResponseBody
    public ResponseEntity<List<Student>> getStudentsByClass(@PathVariable Long classId) {

        // --- SỬA Ở ĐÂY ---
        // Dùng studentRepository thay vì userRepository
        List<Student> students = studentRepository.findBySchoolClass_ClassId(classId);

        return ResponseEntity.ok(students);
    }
}