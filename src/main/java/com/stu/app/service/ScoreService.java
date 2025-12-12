package com.stu.app.service;

import com.stu.app.dto.CourseStatsDTO;
import com.stu.app.model.Score;
import com.stu.app.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    // Lấy điểm của 1 lớp theo môn học
    public List<Score> getScoresByClassAndCourse(Long classId, Long courseId) {
        return scoreRepository.findByClassAndCourse(classId, courseId);
    }

    // Lưu hoặc Cập nhật điểm
    public void saveScore(Score score) {
        scoreRepository.save(score);
    }

    // Xóa điểm
    public void deleteScore(Long scoreId) {
        scoreRepository.deleteById(scoreId);
    }

    // --- LOGIC THỐNG KÊ (Pass Rate, Excellence Rate) ---
    public CourseStatsDTO calculateCourseStats(Long courseId, String courseName) {
        // Lấy danh sách điểm môn này (Có thể tối ưu bằng @Query count DB nếu dữ liệu lớn)
        // Ở đây demo dùng Java Stream để dễ hiểu logic
        List<Score> scores = scoreRepository.findAll().stream()
                .filter(s -> s.getCourse().getCourseId().equals(courseId))
                .toList();

        long total = scores.size();
        if (total == 0) return new CourseStatsDTO(courseName, 0, 0, 0, 0);

        long passCount = scores.stream().filter(s -> s.getScoreValue() >= 60).count();
        long excCount = scores.stream().filter(s -> s.getScoreValue() >= 80).count();
        long distCount = scores.stream().filter(s -> s.getScoreValue() >= 90).count();

        // Làm tròn 2 chữ số thập phân
        double passRate = Math.round((double) passCount / total * 10000.0) / 100.0;
        double excRate = Math.round((double) excCount / total * 10000.0) / 100.0;
        double distRate = Math.round((double) distCount / total * 10000.0) / 100.0;

        return new CourseStatsDTO(courseName, passRate, excRate, distRate, total);
    }

    // --- LOGIC PHÂN PHỐI ĐIỂM (Chart Distribution) ---
    // Trả về Map: "0-59" -> 5 học sinh, "60-79" -> 10 học sinh...
    public Map<String, Integer> getScoreDistribution(Long courseId) {
        List<Score> scores = scoreRepository.findAll().stream()
                .filter(s -> s.getCourse().getCourseId().equals(courseId))
                .toList();

        Map<String, Integer> dist = new HashMap<>();
        dist.put("0-59", 0);
        dist.put("60-69", 0);
        dist.put("70-79", 0);
        dist.put("80-89", 0);
        dist.put("90-100", 0);

        for (Score s : scores) {
            double val = s.getScoreValue();
            if (val < 60) dist.put("0-59", dist.get("0-59") + 1);
            else if (val < 70) dist.put("60-69", dist.get("60-69") + 1);
            else if (val < 80) dist.put("70-79", dist.get("70-79") + 1);
            else if (val < 90) dist.put("80-89", dist.get("80-89") + 1);
            else dist.put("90-100", dist.get("90-100") + 1);
        }
        return dist;
    }
}