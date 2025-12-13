package com.stu.app.repository;

import com.stu.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    // --- GIỮ LẠI HÀM NÀY THEO YÊU CẦU CỦA BẠN ---
    // Nhưng phải thêm @Query để chỉ đường cho Hibernate
    // Logic: Tìm User có ID nằm trong danh sách (User ID của các Student thuộc Class đó)
    @Query("SELECT u FROM User u WHERE u.userId IN " +
            "(SELECT s.user.userId FROM Student s WHERE s.schoolClass.classId = :classId)")
    List<User> findBySchoolClass_ClassId(@Param("classId") Long classId);
}