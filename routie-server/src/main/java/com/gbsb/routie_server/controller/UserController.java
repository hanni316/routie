package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.*;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody SignupRequestDto signupDto) {
        try {
            User newUser = User.builder()
                    .userId(signupDto.getUserId())
                    .email(signupDto.getEmail())
                    .password(signupDto.getPassword())
                    .name(signupDto.getName())
                    .age(signupDto.getAge())
                    .gender(signupDto.getGender())
                    .height(signupDto.getHeight())
                    .weight(signupDto.getWeight())
                    .gold(0)
                    .isAdmin(false)
                    .build();

            userService.createUser(newUser);

            Map<String, String> response = new HashMap<>();
            response.put("message", "회원가입 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "회원가입 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginDto) {
        try {
            // UserService 에서 로그인 검증
            User user = userService.loginUser(loginDto.getUserId(), loginDto.getPassword());

            // 로그인 성공 시 응답 DTO 생성
            LoginResponseDto response = new LoginResponseDto(
                    user.getUserId(),
                    user.getName(),
                    user.getGold()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인 실패: " + e.getMessage());
        }
    }

    // 사용자 정보 변경
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UpdateUserRequestDto updateDto) {
        try {
            User updatedUser = userService.updateUser(userId, User.builder()
                    .email(updateDto.getEmail())
                    .name(updateDto.getName())
                    .age(updateDto.getAge() != null ? updateDto.getAge() : 0)
                    .height(updateDto.getHeight())
                    .weight(updateDto.getWeight())
                    .build());

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("사용자 정보 수정 실패: " + e.getMessage());
        }
    }

    // 비밀번호 변경
    @PutMapping("/users/{userId}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable String userId,
            @RequestBody PasswordUpdateRequestDto dto) {
        try {
            userService.updatePassword(userId, dto.getCurrentPassword(), dto.getNewPassword());
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("비밀번호 변경 실패: " + e.getMessage());
        }
    }

    // 사용자 계정 삭제
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("사용자 계정 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("사용자 삭제 실패: " + e.getMessage());
        }
    }
}
