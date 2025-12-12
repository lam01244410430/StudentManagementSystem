package com.stu.app.dto;

public class CourseStatsDTO {
    private String courseName;
    private double passRate;       // Tỷ lệ đỗ (>60)
    private double excellentRate;  // Tỷ lệ giỏi (>80)
    private double distinctionRate;// Tỷ lệ xuất sắc (>90)
    private long totalStudents;

    // Constructor, Getters, Setters
    public CourseStatsDTO(String courseName, double passRate, double excellentRate, double distinctionRate, long totalStudents) {
        this.courseName = courseName;
        this.passRate = passRate;
        this.excellentRate = excellentRate;
        this.distinctionRate = distinctionRate;
        this.totalStudents = totalStudents;
    }

    public String getCourseName() { return courseName; }
    public double getPassRate() { return passRate; }
    public double getExcellentRate() { return excellentRate; }
    public double getDistinctionRate() { return distinctionRate; }
    public long getTotalStudents() { return totalStudents; }
}