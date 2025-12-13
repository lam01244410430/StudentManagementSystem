package com.stu.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // [THÊM MỚI]
import jakarta.persistence.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.LocalDate;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "student_id", length = 50)
    private String studentId;

    @Column(name = "stu_name", columnDefinition = "NVARCHAR(50)")
    private String fullName;

    @Column(name = "gender", columnDefinition = "NVARCHAR(10)")
    private String gender;

    @Column(name = "birth")
    private LocalDate birth;

    // --- SỬA LỖI JSON TẠI ĐÂY ---
    // Khi lấy Student, ta lấy thông tin Class, nhưng ĐỪNG lấy danh sách students bên trong Class đó nữa.
    // Giả sử bên SchoolClass có biến: private List<Student> students;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnoreProperties("students")
    private SchoolClass schoolClass;

    // Tương tự, tránh lấy ngược lại thông tin dư thừa từ User nếu không cần thiết
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonIgnoreProperties({"password", "role"}) // Ẩn pass khi hiện thông tin student
    private User user;

    public Student() {}

    // ... Giữ nguyên các Getters & Setters như cũ ...
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getBirth() { return birth; }
    public void setBirth(LocalDate birth) { this.birth = birth; }

    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}