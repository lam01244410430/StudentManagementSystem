package com.stu.app.repository;

import com.stu.app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Lưu ý: Key của Student là String (do DB là VARCHAR)
@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    // Tìm Student theo User ID (đã làm ở bước trước)
    Optional<Student> findByUser_UserId(Long userId);

    // --- THÊM DÒNG NÀY ĐỂ SỬA LỖI ---
    // Spring sẽ tự hiểu: Vào Student -> lấy object SchoolClass -> lấy field classId
    List<Student> findBySchoolClass_ClassId(Long classId);
}