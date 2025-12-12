package com.stu.app.controller;

import com.stu.app.dto.CourseStatsDTO;
import com.stu.app.model.Course;
import com.stu.app.model.SchoolClass;
import com.stu.app.model.Score;
import com.stu.app.repository.CourseRepository;
import com.stu.app.repository.SchoolClassRepository;
import com.stu.app.repository.ScoreRepository;
import com.stu.app.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class TeacherController {

    @Autowired private SchoolClassRepository classRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private ScoreService scoreService;
    @Autowired private ScoreRepository scoreRepository; // Dùng tạm repo để tìm nhanh

    @GetMapping("/teacher/dashboard")
    public String dashboard(
            Model model,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Long subjectId
    ) {
        // 1. Load danh sách bộ lọc
        List<SchoolClass> classes = classRepository.findAll();
        List<Course> subjects = courseRepository.findAll();
        model.addAttribute("classes", classes);
        model.addAttribute("subjects", subjects);

        // 2. Xử lý lựa chọn hiện tại
        if (classId != null) {
            model.addAttribute("selectedClass", classRepository.findById(classId).orElse(null));
        }
        if (subjectId != null) {
            Course subject = courseRepository.findById(subjectId).orElse(null);
            model.addAttribute("selectedSubject", subject);

            // 3. Tính toán thống kê NẾU đã chọn môn
            if (subject != null) {
                CourseStatsDTO stats = scoreService.calculateCourseStats(subjectId, subject.getCourseName());
                model.addAttribute("stats", stats);

                // Biểu đồ phân phối điểm
                Map<String, Integer> distributionRaw = scoreService.getScoreDistribution(subjectId);
                // Convert map sang List object để Thymeleaf dễ duyệt
                List<Map<String, Object>> distList = new ArrayList<>();
                distributionRaw.forEach((k, v) -> distList.add(Map.of("label", k, "count", v, "percentage", (v * 10)))); // % giả định để vẽ cột
                model.addAttribute("distribution", distList);
            }
        }

        // 4. Danh sách bảng điểm (Grades Table)
        if (classId != null && subjectId != null) {
            List<Score> scores = scoreService.getScoresByClassAndCourse(classId, subjectId);
            model.addAttribute("grades", scores);

            // Top students logic đơn giản (Lấy 3 người điểm cao nhất)
            model.addAttribute("topStudents", scores.stream()
                    .sorted((s1, s2) -> Double.compare(s2.getScoreValue(), s1.getScoreValue()))
                    .limit(3).toList());
        } else {
            model.addAttribute("grades", new ArrayList<>());
        }

        model.addAttribute("currentPage", "dashboard");
        return "teacher/dashboard"; // templates/teacher/dashboard.html
    }

    // API Lưu điểm từ Modal nhập liệu
    @PostMapping("/teacher/grades/save")
    public String saveGrade(
            @RequestParam Long classId,
            @RequestParam Long subjectId,
            @RequestParam String studentIdentifier, // ID hoặc Tên
            @RequestParam Double score
    ) {
        // Logic tìm Student và Save Score (Sẽ cần hoàn thiện tìm kiếm chính xác)
        // Đây là demo redirect lại trang dashboard
        return "redirect:/teacher/dashboard?classId=" + classId + "&subjectId=" + subjectId;
    }
}