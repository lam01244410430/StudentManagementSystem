package com.stu.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Class") // Đổi tên bảng thành 'classes' để tránh lỗi SQL keyword
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classId;

    @Column(name = "class_name", columnDefinition = "NVARCHAR(255)")
    private String className; // Tên lớp

    public SchoolClass() {}

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}