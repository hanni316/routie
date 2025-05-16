package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.Reward;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.dto.UserProfileResponseDto;
import com.gbsb.routie_server.repository.RewardRepository;
import com.gbsb.routie_server.repository.RoutineRepository;
import com.gbsb.routie_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;
    private final RoutineRepository routineRepository;

    // 회원가입
    @Transactional
    public User createUser(User user) {
        // 중복 ID 확인
        if (userRepository.findById(user.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 중복 이메일 확인
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
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
    public boolean isUserIdAvailable(String userId) {
        return !userRepository.findById(userId).isPresent();
    }


    // 로그인
    public User loginUser(String userId, String password) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 ID의 사용자가 없습니다.");
        }
    }

    // 사용자 정보 수정
    @Transactional
    public User updateUser(String userId, User updatedUser) {
        return userRepository.findById(userId)
                .map(user -> {
                    if (updatedUser.getEmail() != null) {
                        user.setEmail(updatedUser.getEmail());
                    }
                    if (updatedUser.getName() != null) {
                        user.setName(updatedUser.getName());
                    }
                    if (updatedUser.getAge() != 0) {
                        user.setAge(updatedUser.getAge());
                    }
                    if (updatedUser.getHeight() != 0) {
                        user.setHeight(updatedUser.getHeight());
                    }
                    if (updatedUser.getWeight() != 0) {
                        user.setWeight(updatedUser.getWeight());
                    }
                    return userRepository.save(user);
                }).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void updatePassword(String userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!user.getPassword().equals(currentPassword)) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(newPassword);
        userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        return new UserProfileResponseDto(
                user.getUserId(),
                user.getName(),
                user.getGold()
        );
    }

    // 사용자 조회
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // 사용자 삭제 (회원 탈퇴)
    @Transactional
    public void deleteUser(String userId) {
        routineRepository.deleteByUser_UserId(userId); // 관련 루틴 삭제
        rewardRepository.deleteByUser_UserId(userId); // 관련 리워드 삭제

        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }

    // 프로필 사진 업데이트
    public String updateProfileImage(String userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String uploadDirPath = System.getProperty("user.dir") + "/uploads/profile/";
        // System.getProperty("user.dir") : 현재 프로젝트 루트 절대경로를 가져옴
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = uploadDirPath + fileName; // 저장할 경로

        try {
            // 폴더 없으면 자동 생성
            File dir = new File(uploadDirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // 저장된 파일 경로를 DB에 저장
        String relativePath = "/uploads/profile/" + fileName;

        user.setProfileImageUrl(relativePath);
        userRepository.save(user);

        return relativePath;
    }
}
