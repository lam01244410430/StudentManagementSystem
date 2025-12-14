package com.stu.app.service;

import com.stu.app.dto.UserDTO;
import com.stu.app.model.*;
import com.stu.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private SchoolClassRepository classRepository; // You need this repo

    // 1. Get All Users
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(convertToDTO(u));
        }
        return dtos;
    }

    // 2. Create User
    @Transactional
    public UserDTO createUser(UserDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // Hash this in production!

        Role role = roleRepository.findByRoleName(dto.getRole().getRoleName());
        user.setRole(role);

        user = userRepository.save(user);

        if ("STUDENT".equals(role.getRoleName())) {
            saveStudentInfo(user, dto);
        }
        return convertToDTO(user);
    }

    // 3. Update User
    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id).orElseThrow();

        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }

        Role role = roleRepository.findByRoleName(dto.getRole().getRoleName());
        user.setRole(role);

        user = userRepository.save(user);

        if ("STUDENT".equals(role.getRoleName())) {
            saveStudentInfo(user, dto);
        }
        return convertToDTO(user);
    }

    // 4. Delete User
    @Transactional
    public void deleteUser(Long id) {
        // Delete student record first to avoid FK constraint
        Optional<Student> student = studentRepository.findByUser_UserId(id);
        student.ifPresent(studentRepository::delete);

        userRepository.deleteById(id);
    }

    // Helper: Save Student Info
    private void saveStudentInfo(User user, UserDTO dto) {
        Student student = studentRepository.findByUser_UserId(user.getUserId())
                .orElse(new Student());

        student.setUser(user);
        student.setStudentId(user.getUsername()); // Reuse username as StudentID
        student.setFullName(dto.getFullName());
        student.setGender(dto.getGender());
        student.setBirth(dto.getBirth());

        if (dto.getSchoolClass() != null && dto.getSchoolClass().getClassId() != null) {
            SchoolClass sc = classRepository.findById(dto.getSchoolClass().getClassId()).orElse(null);
            student.setSchoolClass(sc);
        }
        studentRepository.save(student);
    }

    // Helper: Convert Entity to DTO
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());

        UserDTO.RoleDTO rDto = new UserDTO.RoleDTO();
        rDto.setRoleName(user.getRole().getRoleName());
        dto.setRole(rDto);

        // Check if Student info exists
        Optional<Student> sOpt = studentRepository.findByUser_UserId(user.getUserId());
        if (sOpt.isPresent()) {
            Student s = sOpt.get();
            dto.setFullName(s.getFullName());
            dto.setGender(s.getGender());
            dto.setBirth(s.getBirth());

            if (s.getSchoolClass() != null) {
                UserDTO.ClassDTO cDto = new UserDTO.ClassDTO();
                cDto.setClassId(s.getSchoolClass().getClassId());
                cDto.setClassName(s.getSchoolClass().getClassName());
                dto.setSchoolClass(cDto);
            }
        }
        return dto;
    }
}