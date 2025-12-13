package com.stu.app.repository;

import com.stu.app.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByStudent_StudentId(String studentId);

    // --- SỬA TẠI ĐÂY: Thêm logic (:param IS NULL OR ...) ---
    @Query("SELECT s FROM Score s WHERE " +
            "(:classId IS NULL OR s.student.schoolClass.classId = :classId) AND " +
            "(:courseId IS NULL OR s.course.courseId = :courseId)")
    List<Score> findByClassAndCourse(@Param("classId") Long classId, @Param("courseId") Long courseId);

    // Tìm điểm để check trùng (dùng cho Service saveScore)
    @Query("SELECT s FROM Score s WHERE s.student.studentId = :studentId")
    List<Score> findByStudentId(@Param("studentId") String studentId);
}