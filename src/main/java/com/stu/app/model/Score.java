package com.stu.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime; // Chú ý import này

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
    private LocalDateTime examTime; // Kiểu dữ liệu là LocalDateTime

    // Các quan hệ khác...
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "input_by_user_id")
    private User inputByUser;

    public Score() {}

    // --- BẮT BUỘC PHẢI CÓ CÁC HÀM NÀY ĐỂ HẾT LỖI ---
    public LocalDateTime getExamTime() {
        return examTime;
    }

    public void setExamTime(LocalDateTime examTime) {
        this.examTime = examTime;
    }

    public Long getScoreId() { return scoreId; }
    public void setScoreId(Long scoreId) { this.scoreId = scoreId; }

    public Double getScoreValue() { return scoreValue; }
    public void setScoreValue(Double scoreValue) { this.scoreValue = scoreValue; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}