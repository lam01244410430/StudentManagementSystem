package com.stu.app.controller;

import com.stu.app.model.Course;
import com.stu.app.model.User;
import com.stu.app.repository.CourseRepository;
import com.stu.app.model.Score;
import com.stu.app.model.Student;
import com.stu.app.repository.ScoreRepository;
import com.stu.app.repository.StudentRepository;
import com.stu.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class StudentController {

    @Autowired private StudentRepository studentRepository;
    @Autowired private ScoreRepository scoreRepository;
    @Autowired private UserRepository userRepository;

    // View HTML
    @GetMapping("/student/dashboard")
    public String dashboard(Model model, Principal principal) {
        // Lấy User đang đăng nhập
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        // Gửi thông tin xuống View
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("username", username);

        return "student/dashboard";
    }

    // API: Lấy thông tin Student từ UserID (để JS gọi)
    @ResponseBody
    @GetMapping("/api/students/by-user/{userId}")
    public Student getStudentByUserId(@PathVariable Long userId) {
        return studentRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // API: Lấy bảng điểm của Student (để JS gọi)
    @ResponseBody
    @GetMapping("/api/scores/student/{studentId}")
    public List<Score> getStudentScores(@PathVariable String studentId) {
        return scoreRepository.findByStudent_StudentId(studentId);
    }

    // API: Lấy danh sách môn học (để hiển thị cả môn chưa có điểm)
    @Autowired private CourseRepository courseRepo;
    @ResponseBody
    @GetMapping("/api/courses")
    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }
}