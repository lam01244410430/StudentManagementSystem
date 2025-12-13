package com.stu.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "class") // Tên bảng trong DB là 'classes'
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classId;

    @Column(name = "class_name", columnDefinition = "NVARCHAR(255)")
    private String className;

    public SchoolClass() {}

    // Constructor có tham số để tiện tạo đối tượng
    public SchoolClass(String className) {
        this.className = className;
    }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}