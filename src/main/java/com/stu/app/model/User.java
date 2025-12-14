package com.stu.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "[user]") // SQL Server cần ngoặc vuông cho bảng user
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    // --- MAPPING ROLE (3 Roles: Admin, Teacher, Student) ---
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // --- SỬA TẠI ĐÂY: Đổi tên biến thành 'studentInfo' để khớp với lỗi ---
    // mappedBy = "user": Trỏ tới biến 'private User user' bên class Student
    // optional = true: Vì Teacher và Admin sẽ KHÔNG có thông tin này (null)
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user", "scores"})
    private Student studentInfo;

    public User() {}

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // Getter/Setter phải khớp tên biến studentInfo
    public Student getStudentInfo() { return studentInfo; }
    public void setStudentInfo(Student studentInfo) { this.studentInfo = studentInfo; }
}