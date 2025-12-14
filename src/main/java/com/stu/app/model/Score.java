package com.stu.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Thêm import này
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "score")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long scoreId;

    @Column(name = "score_value")
    private Double scoreValue;

    @Column(name = "exam_time")
    private LocalDateTime examTime;

    // --- FIX VÒNG LẶP JSON ---
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({"user", "schoolClass", "scores"}) // FIX: Ngăn chặn vòng lặp qua Student
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnoreProperties("scores") // FIX: Ngăn chặn list scores trong Course (nếu có)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "input_by_user_id")
    @JsonIgnoreProperties({"student", "role", "password"}) // FIX: Ngăn chặn vòng lặp qua User
    private User inputByUser;
    // ... (Getters/Setters giữ nguyên)
    public Score() {}
    public LocalDateTime getExamTime() { return examTime; }
    public void setExamTime(LocalDateTime examTime) { this.examTime = examTime; }
    public Long getScoreId() { return scoreId; }
    public void setScoreId(Long scoreId) { this.scoreId = scoreId; }
    public Double getScoreValue() { return scoreValue; }
    public void setScoreValue(Double scoreValue) { this.scoreValue = scoreValue; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public User getInputByUser() { return inputByUser; }
    public void setInputByUser(User inputByUser) { this.inputByUser = inputByUser; }
}