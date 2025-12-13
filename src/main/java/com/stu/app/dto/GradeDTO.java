package com.stu.app.dto;

public class GradeDTO {
    private Long id;          // ID điểm
    private String studentId; // --- SỬA TẠI ĐÂY: Đổi Long thành String ---
    private String studentName;
    private String className;
    private Long classId;
    private Long courseId;
    private String courseName;
    private Double score;
    private int rank;

    public GradeDTO() {}

    // --- SỬA CONSTRUCTOR: Tham số thứ 2 đổi thành String ---
    public GradeDTO(Long id, String studentId, String studentName, String className,
                    Long classId, Long courseId, String courseName, Double score) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.className = className;
        this.classId = classId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.score = score;
    }

    // --- SỬA GETTER/SETTER ---
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    // Các getter/setter khác giữ nguyên
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
}