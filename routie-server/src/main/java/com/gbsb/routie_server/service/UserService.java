package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    public User createUser(User user) {
        // 중복 이메일 확인
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        return userRepository.save(user);
    }

    // 로그인
    public User loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {  // 비밀번호 검증
                return user;  // 로그인 성공
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 이메일의 사용자가 없습니다.");
        }
    }
}
