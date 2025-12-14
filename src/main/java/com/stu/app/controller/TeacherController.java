package com.stu.app.controller;

import com.stu.app.dto.GradeDTO;
import com.stu.app.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
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

        // --- SỬA LỖI: XÁC ĐỊNH CHẾ ĐỘ TRUNG BÌNH ---
        // isAverageMode = (Subject là ALL) AND (Class là ALL hoặc Class được chọn)
        boolean isAverageMode = (subjectId == null);
        model.addAttribute("isAverageMode", isAverageMode);


        // Lấy danh sách điểm
        List<GradeDTO> grades = teacherService.getGrades(classId, subjectId, keyword);
        model.addAttribute("grades", grades);

        // Các thống kê chỉ hoạt động khi có data
        if (!grades.isEmpty()) {
            // Tính toán Thống kê (4 ô vuông)
            Map<String, Object> stats = teacherService.calculateStats(grades);
            model.addAttribute("stats", stats);

            // Tính toán Biểu đồ
            model.addAttribute("distribution", teacherService.getDistribution(grades));

            // Tính toán Top/Bottom students
            model.addAttribute("topStudents", grades.stream().limit(5).collect(Collectors.toList()));
            List<GradeDTO> bottom = grades.stream()
                    .sorted(Comparator.comparingDouble(GradeDTO::getScore))
                    .limit(5)
                    .collect(Collectors.toList());
            model.addAttribute("bottomStudents", bottom);
        } else {
            model.addAttribute("stats", null);
            model.addAttribute("distribution", null);
            model.addAttribute("topStudents", List.of());
            model.addAttribute("bottomStudents", List.of());
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