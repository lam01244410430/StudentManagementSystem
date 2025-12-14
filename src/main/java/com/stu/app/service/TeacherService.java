package com.stu.app.service;

import com.stu.app.dto.GradeDTO;
import com.stu.app.model.*;
import com.stu.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired private ScoreRepository scoreRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SchoolClassRepository classRepository;
    @Autowired private CourseRepository courseRepository;

    // --- SỬA HÀM getGrades ĐỂ ĐIỀU HƯỚNG LOGIC ---
    public List<GradeDTO> getGrades(Long classId, Long courseId, String keyword) {
        List<GradeDTO> dtos;

        // KIỂM TRA CHẾ ĐỘ ĐIỂM TRUNG BÌNH: courseId là NULL (All Subjects), ClassId có thể là NULL hoặc Long
        if (courseId == null) {
            dtos = getAverageGrades(classId, keyword); // Dùng hàm tính TB mới
        } else {
            // CHẾ ĐỘ MÔN HỌC CỤ THỂ (Specific Subject Mode)
            dtos = getSpecificGrades(classId, courseId, keyword);
        }

        // Tính Rank lại từ 1 đến N (cho cả 2 chế độ)
        dtos.sort(Comparator.comparingDouble(GradeDTO::getScore).reversed());
        for (int i = 0; i < dtos.size(); i++) {
            dtos.get(i).setRank(i + 1);
        }
        return dtos;
    }

    // --- HÀM MỚI: Xử lý điểm cho MỘT MÔN CỤ THỂ (Specific Subject Mode) ---
    private List<GradeDTO> getSpecificGrades(Long classId, Long courseId, String keyword) {
        List<Score> scores = scoreRepository.findByClassAndCourse(classId, courseId);

        if (keyword != null && !keyword.isEmpty()) {
            String lowerKey = keyword.toLowerCase();
            scores = scores.stream()
                    .filter(s -> s.getStudent().getFullName().toLowerCase().contains(lowerKey) ||
                            s.getStudent().getStudentId().toLowerCase().contains(lowerKey))
                    .collect(Collectors.toList());
        }

        return scores.stream()
                .map(s -> new GradeDTO(
                        s.getScoreId(),
                        s.getStudent().getStudentId(),
                        s.getStudent().getFullName(),
                        s.getStudent().getSchoolClass().getClassName(),
                        s.getStudent().getSchoolClass().getClassId(),
                        s.getCourse().getCourseId(),
                        s.getCourse().getCourseName(),
                        s.getScoreValue()
                ))
                .collect(Collectors.toList());
    }

    // --- HÀM MỚI: Xử lý điểm TRUNG BÌNH (Average Mode) ---
    private List<GradeDTO> getAverageGrades(Long classId, String keyword) {
        // 1. Lấy tất cả học sinh (có thể được filter theo ClassId)
        List<Student> students;
        if (classId != null) {
            // Nếu có ClassId, chỉ lấy học sinh trong lớp đó
            students = studentRepository.findBySchoolClass_ClassId(classId);
        } else {
            // Nếu ClassId là NULL (All Classes), lấy tất cả học sinh
            students = studentRepository.findAll();
        }

        // 2. Lấy tất cả điểm số liên quan đến các học sinh này (CourseId = NULL)
        // Dùng classId trong findByClassAndCourse. Nếu classId là NULL, ScoreRepository sẽ lấy TẤT CẢ điểm.
        List<Score> allScores = scoreRepository.findByClassAndCourse(classId, null);

        // 3. Nhóm điểm theo StudentId
        Map<String, List<Score>> scoresByStudent = allScores.stream()
                .collect(Collectors.groupingBy(s -> s.getStudent().getStudentId()));

        // 4. Tính điểm trung bình và tạo GradeDTO
        List<GradeDTO> dtos = students.stream()
                .map(student -> {
                    List<Score> studentScores = scoresByStudent.getOrDefault(student.getStudentId(), Collections.emptyList());

                    double averageScore = studentScores.stream()
                            .mapToDouble(Score::getScoreValue)
                            .average()
                            .orElse(0.0);

                    GradeDTO dto = new GradeDTO();
                    dto.setStudentId(student.getStudentId());
                    dto.setStudentName(student.getFullName());

                    // Xử lý SchoolClass
                    if(student.getSchoolClass() != null) {
                        dto.setClassName(student.getSchoolClass().getClassName());
                        dto.setClassId(student.getSchoolClass().getClassId());
                    } else {
                        dto.setClassName("N/A"); // Default value
                    }

                    dto.setScore(Double.valueOf(String.format("%.1f", averageScore))); // Làm tròn 1 chữ số thập phân
                    dto.setCourseName("AVERAGE"); // Đánh dấu đây là điểm trung bình

                    return dto;
                })
                .filter(dto -> keyword == null || keyword.isEmpty() ||
                        dto.getStudentName().toLowerCase().contains(keyword.toLowerCase()) ||
                        dto.getStudentId().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        return dtos;
    }

    // ... Giữ nguyên các hàm calculateStats, getDistribution, saveScore, deleteScore, helpers ...

    // 2. Tính toán thống kê (Giữ nguyên logic toán học)
    public Map<String, Object> calculateStats(List<GradeDTO> grades) {
        if (grades == null || grades.isEmpty()) return null; // Thêm check null

        double avg = grades.stream().mapToDouble(GradeDTO::getScore).average().orElse(0.0);
        long total = grades.size();
        long pass = grades.stream().filter(g -> g.getScore() >= 50).count();
        long exc = grades.stream().filter(g -> g.getScore() >= 80).count();
        long dist = grades.stream().filter(g -> g.getScore() >= 90).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("averageScore", String.format("%.1f", avg));
        stats.put("passRate", total > 0 ? (pass * 100) / total : 0);
        stats.put("excellenceRate", total > 0 ? (exc * 100) / total : 0);
        stats.put("distinctionRate", total > 0 ? (dist * 100) / total : 0);

        stats.put("avgRank", 1); stats.put("passRank", 1);
        stats.put("excellenceRank", 1); stats.put("distinctionRank", 1);
        return stats;
    }

    // 3. Tính phân phối biểu đồ cột (Giữ nguyên)
    public List<Map<String, Object>> getDistribution(List<GradeDTO> grades) {
        if (grades == null || grades.isEmpty()) return null;
        long total = grades.size();

        int fail = 0, pass = 0, merit = 0, dist = 0;
        for (GradeDTO g : grades) {
            double s = g.getScore();
            if (s < 60) fail++;
            else if (s >= 60 && s < 80) pass++;
            else if (s >= 80 && s < 90) merit++;
            else dist++;
        }

        return List.of(
                createBar("Fail (<60)", fail, total),
                createBar("Pass (60-79)", pass, total),
                createBar("Merit (80-89)", merit, total),
                createBar("Distinction (>=90)", dist, total)
        );
    }

    private Map<String, Object> createBar(String label, int count, long total) {
        Map<String, Object> m = new HashMap<>();
        m.put("label", label);
        m.put("count", count);
        m.put("percentage", total > 0 ? (count * 100) / total : 0);
        m.put("rangeStart", label.contains("Fail") ? 0 : 90);
        return m;
    }

    // 4. Lưu điểm (Create/Update)
    public void saveScore(Long scoreId, Long subjectId, String studentIdentifier, Double val) {
        Score score;

        // Trường hợp cập nhật điểm đã có (scoreId truyền vào khác null)
        if (scoreId != null) {
            score = scoreRepository.findById(scoreId).orElseThrow();
        } else {
            // Trường hợp nhập mới
            Student student = studentRepository.findById(studentIdentifier)
                    .orElseThrow(() -> new RuntimeException("Student not found: " + studentIdentifier));

            Course course = courseRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            List<Score> existingScores = scoreRepository.findByStudent_StudentId(student.getStudentId());
            Optional<Score> exist = existingScores.stream()
                    .filter(s -> s.getCourse().getCourseId().equals(subjectId))
                    .findFirst();

            if (exist.isPresent()) {
                score = exist.get(); // Nếu đã có điểm môn này rồi thì update
            } else {
                score = new Score(); // Nếu chưa có thì tạo mới
                score.setStudent(student);
                score.setCourse(course);
            }
        }

        score.setScoreValue(val);
        score.setExamTime(LocalDateTime.now());

        scoreRepository.save(score);
    }

    // Helpers
    public List<SchoolClass> getAllClasses() { return classRepository.findAll(); }
    public List<Course> getAllCourses() { return courseRepository.findAll(); }
    public void deleteScore(Long id) { scoreRepository.deleteById(id); }
}