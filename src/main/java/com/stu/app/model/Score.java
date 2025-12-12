package com.stu.app.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID nội bộ của bảng điểm

    // Mối quan hệ với Student
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Mối quan hệ với Course
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Người nhập điểm (Giáo viên) - input_by_user_id
    @ManyToOne
    @JoinColumn(name = "input_by_user_id")
    private User inputBy;

    @Column(name = "score_value")
    private Double scoreValue;

    @Column(name = "exam_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date examTime;

    public Score() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public User getInputBy() { return inputBy; }
    public void setInputBy(User inputBy) { this.inputBy = inputBy; }
    public Double getScoreValue() { return scoreValue; }
    public void setScoreValue(Double scoreValue) { this.scoreValue = scoreValue; }
    public Date getExamTime() { return examTime; }
    public void setExamTime(Date examTime) { this.examTime = examTime; }
}