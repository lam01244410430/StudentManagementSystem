package com.stu.app.controller;

import com.stu.app.dto.GradeDTO;
import com.stu.app.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    // 1. Route chính cho Dashboard
    @GetMapping("/teacher/dashboard")
    public String dashboard(
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) String keyword,
            Model model) {

        // Load dữ liệu Classes và Subjects cho Dropdown
        model.addAttribute("classes", teacherService.getAllClasses());
        model.addAttribute("subjects", teacherService.getAllCourses());

        // Giữ lại trạng thái filter đã chọn để hiển thị trên View
        model.addAttribute("selectedClassId", classId);
        model.addAttribute("selectedSubjectId", subjectId);
        model.addAttribute("keyword", keyword);

        // Lấy danh sách điểm (Nếu classId, subjectId là null -> Lấy tất cả)
        List<GradeDTO> grades = teacherService.getGrades(classId, subjectId, keyword);
        model.addAttribute("grades", grades);

        // Tính toán Thống kê (4 ô vuông)
        Map<String, Object> stats = teacherService.calculateStats(grades);
        model.addAttribute("stats", stats);

        // Tính toán Biểu đồ
        model.addAttribute("distribution", teacherService.getDistribution(grades));

        // Tính toán Top/Bottom students (Lấy 5 người đầu và 5 người cuối)
        if (!grades.isEmpty()) {
            model.addAttribute("topStudents", grades.stream().limit(5).collect(Collectors.toList()));
            // Lấy những người điểm thấp (đảo ngược list hoặc lấy đuôi)
            List<GradeDTO> bottom = grades.stream()
                    .sorted((g1, g2) -> Double.compare(g1.getScore(), g2.getScore())) // Sắp xếp tăng dần để lấy thấp nhất
                    .limit(5)
                    .collect(Collectors.toList());
            model.addAttribute("bottomStudents", bottom);
        }

        return "teacher/dashboard"; // Trả về file HTML của bạn
    }

    // 2. Route xử lý yêu cầu "/grades" -> Redirect về dashboard
    @GetMapping("/grades")
    public String redirectToDashboard() {
        return "redirect:/teacher/dashboard";
    }

    // 3. API lưu điểm (Single Input)
    @PostMapping("/teacher/grades/save")
    public String saveGrade(
            @RequestParam(required = false) Long id,
            @RequestParam Long classId,
            @RequestParam Long subjectId,
            @RequestParam String studentIdentifier, // Student ID dạng String
            @RequestParam Double score) {

        teacherService.saveScore(id, subjectId, studentIdentifier, score);

        // Redirect lại trang dashboard kèm filter class cũ để tiện nhập tiếp
        return "redirect:/teacher/dashboard?classId=" + classId + "&subjectId=" + subjectId;
    }

    // 4. API Xóa điểm
    @GetMapping("/teacher/grades/delete/{id}")
    public String deleteGrade(@PathVariable Long id) {
        teacherService.deleteScore(id);
        return "redirect:/teacher/dashboard";
    }
}