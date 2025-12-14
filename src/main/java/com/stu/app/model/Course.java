package com.stu.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Thêm import này
import jakarta.persistence.*;

@Entity
@Table(name = "Course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_name", columnDefinition = "NVARCHAR(255)")
    private String courseName;

    @Column(name = "credits")
    private Integer credits;

    // --- THÊM VÀO NẾU CÓ LIST SCORE TRONG COURSE ---
    // @OneToMany(mappedBy = "course")
    // @JsonIgnoreProperties("course")
    // private List<Score> scores;

    public Course() {}
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
}