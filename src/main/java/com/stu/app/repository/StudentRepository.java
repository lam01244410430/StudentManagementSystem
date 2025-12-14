package com.stu.app.repository;

import com.stu.app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    // Tìm Student theo User ID
    Optional<Student> findByUser_UserId(Long userId);

    // --- QUAN TRỌNG: Hàm này giúp lấy danh sách học sinh theo Class ID ---
    List<Student> findBySchoolClass_ClassId(Long classId);
}