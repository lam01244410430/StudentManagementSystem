package com.stu.app.dto;

import java.time.LocalDate;

public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String fullName;
    private String gender;
    private LocalDate birth;

    // Nested objects to match your JS payload
    private RoleDTO role;
    private ClassDTO schoolClass;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getBirth() { return birth; }
    public void setBirth(LocalDate birth) { this.birth = birth; }
    public RoleDTO getRole() { return role; }
    public void setRole(RoleDTO role) { this.role = role; }
    public ClassDTO getSchoolClass() { return schoolClass; }
    public void setSchoolClass(ClassDTO schoolClass) { this.schoolClass = schoolClass; }

    public static class RoleDTO {
        private String roleName;
        public String getRoleName() { return roleName; }
        public void setRoleName(String roleName) { this.roleName = roleName; }
    }

    public static class ClassDTO {
        private Long classId;
        private String className;
        public Long getClassId() { return classId; }
        public void setClassId(Long classId) { this.classId = classId; }
        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
    }
}