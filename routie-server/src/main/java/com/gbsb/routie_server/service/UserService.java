package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.Reward;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.repository.RewardRepository;
import com.gbsb.routie_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;

    // 회원가입
    @Transactional
    public User createUser(User user) {
        // 중복 이메일 확인
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 저장된 사용자 객체를 변수에 저장
        User savedUser = userRepository.save(user);

        // 회원가입 후 보상 데이터 생성
        Reward reward = new Reward();
        reward.setUser(savedUser);
        reward.setTotalGold(0); // 초기 골드 0
        reward.setTotalSteps(0); // 초기 걸음 수 0
        rewardRepository.save(reward); // 보상 데이터 저장

        return savedUser;
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
