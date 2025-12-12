package com.stu.app.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Student")
public class Student {
    @Id
    // Nếu student_id là mã nhập tay (VD: 2025101), bỏ @GeneratedValue
    // Nếu là số tự tăng, giữ nguyên @GeneratedValue
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "stu_name", columnDefinition = "NVARCHAR(255)")
    private String stuName; // Tên SV

    @Column(name = "gender", columnDefinition = "NVARCHAR(10)")
    private String gender; // Giới tính

    @Column(name = "birth")
    @Temporal(TemporalType.DATE)
    private Date birth; // Ngày sinh

    @ManyToOne
    @JoinColumn(name = "class_id") // FK tới Class
    private SchoolClass schoolClass;

    @OneToOne // Một sinh viên gắn với một tài khoản User (để đăng nhập)
    @JoinColumn(name = "user_id")
    private User user;

    public Student() {}

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStuName() { return stuName; }
    public void setStuName(String stuName) { this.stuName = stuName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Date getBirth() { return birth; }
    public void setBirth(Date birth) { this.birth = birth; }
    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}