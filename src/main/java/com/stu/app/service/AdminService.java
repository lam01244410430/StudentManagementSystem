package com.stu.app.service;

import com.stu.app.model.Role;
import com.stu.app.model.SchoolClass;
import com.stu.app.model.User;
import com.stu.app.repository.RoleRepository;
import com.stu.app.repository.SchoolClassRepository;
import com.stu.app.repository.UserRepository;
import com.stu.app.model.*;
import com.stu.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    @Autowired private UserRepository userRepository;
    @Autowired private SchoolClassRepository classRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder; // Sẽ cấu hình ở bước sau

    public List<User> getAllUsers() { return userRepository.findAll(); }
    public List<SchoolClass> getAllClasses() { return classRepository.findAll(); }

    // Tạo User mới (Mã hóa pass)
    public User createUser(User user, String rawPassword, String roleName) {
        user.setPassword(passwordEncoder.encode(rawPassword));
        Role role = roleRepository.findByRoleName(roleName);
        user.setRole(role);
        return userRepository.save(user);
    }

    // Xóa User
    public void deleteUser(Long id) { userRepository.deleteById(id); }
}