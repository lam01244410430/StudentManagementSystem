package com.stu.app.repository;

import com.stu.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm user bằng username để đăng nhập
    Optional<User> findByUsername(String username);
}