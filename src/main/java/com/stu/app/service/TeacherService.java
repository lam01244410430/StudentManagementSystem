package com.stu.app.service;

import com.stu.app.dto.GradeDTO;
import com.stu.app.model.*;
import com.stu.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; // Sửa: Dùng LocalDateTime thay vì Date
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired private ScoreRepository scoreRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SchoolClassRepository classRepository;
    @Autowired private CourseRepository courseRepository;

    // 1. Lấy danh sách điểm và convert sang GradeDTO
    public List<GradeDTO> getGrades(Long classId, Long courseId, String keyword) {
        // Repository giờ đã xử lý được NULL -> Trả về list đầy đủ nếu không chọn filter
        List<Score> scores = scoreRepository.findByClassAndCourse(classId, courseId);

        if (keyword != null && !keyword.isEmpty()) {
            String lowerKey = keyword.toLowerCase();
            scores = scores.stream()
                    .filter(s -> s.getStudent().getFullName().toLowerCase().contains(lowerKey) ||
                            s.getStudent().getStudentId().toLowerCase().contains(lowerKey))
                    .collect(Collectors.toList());
        }

        List<GradeDTO> dtos = scores.stream()
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
                .sorted(Comparator.comparingDouble(GradeDTO::getScore).reversed())
                .collect(Collectors.toList());

        // Tính Rank lại từ 1 đến N
        for (int i = 0; i < dtos.size(); i++) {
            dtos.get(i).setRank(i + 1);
        }
        return dtos;
    }

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
            if (s < 50) fail++;
            else if (s < 70) pass++;
            else if (s < 90) merit++;
            else dist++;
        }

        return List.of(
                createBar("Fail (<50)", fail, total),
                createBar("Pass (50-70)", pass, total),
                createBar("Merit (70-90)", merit, total),
                createBar("Distinction (>90)", dist, total)
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
            // SỬA: studentIdentifier là String (VD: "SV001"), không cần parseLong
            Student student = studentRepository.findById(studentIdentifier)
                    .orElseThrow(() -> new RuntimeException("Student not found: " + studentIdentifier));

            Course course = courseRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // SỬA: Logic tìm điểm cũ để tránh duplicate
            // Dùng hàm findByStudent_StudentId đã có, sau đó lọc Stream để tìm môn học
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
        // SỬA: Dùng LocalDateTime thay vì Date
        score.setExamTime(LocalDateTime.now());

        scoreRepository.save(score);
    }

    // Helpers
    public List<SchoolClass> getAllClasses() { return classRepository.findAll(); }
    public List<Course> getAllCourses() { return courseRepository.findAll(); }
    public void deleteScore(Long id) { scoreRepository.deleteById(id); }
}