package com.stu.app.repository;

import com.stu.app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    // Tìm sinh viên dựa trên tài khoản User đăng nhập (cho Student Dashboard)
    Optional<Student> findByUser_UserId(Long userId);

    // Tìm sinh viên theo lớp (cho Teacher Dashboard)
    List<Student> findBySchoolClass_ClassId(Long classId);
}