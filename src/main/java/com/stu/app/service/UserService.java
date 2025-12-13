package com.stu.app.service;

import com.stu.app.model.User;
import com.stu.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        // .orElse(null) nghĩa là: Nếu có user thì lấy, nếu không có thì trả về null
        return userRepository.findByUsername(username).orElse(null);
    }
}