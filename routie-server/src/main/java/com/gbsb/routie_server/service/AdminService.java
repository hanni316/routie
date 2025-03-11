package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 모든 사용자 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 특정 id로 사용자 조회
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}
