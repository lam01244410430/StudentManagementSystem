package com.stu.app.repository;

import com.stu.app.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    // Lấy tất cả điểm của một sinh viên cụ thể (cho Student Dashboard)
    List<Score> findByStudent_StudentId(String studentId);

    // Lấy điểm của một lớp theo một môn học (cho Teacher nhập điểm/xem điểm)
    @Query("SELECT s FROM Score s WHERE s.student.schoolClass.classId = :classId AND s.course.courseId = :courseId")
    List<Score> findByClassAndCourse(@Param("classId") Long classId, @Param("courseId") Long courseId);

    // Thống kê điểm số theo môn học (cho biểu đồ Teacher)
    // Ví dụ: Đếm số lượng sinh viên đạt điểm Giỏi (>80) của một môn
    @Query("SELECT count(s) FROM Score s WHERE s.course.courseId = :courseId AND s.scoreValue >= :minScore")
    long countByCourseAndScoreGreaterThan(@Param("courseId") Long courseId, @Param("minScore") double minScore);
}