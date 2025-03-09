package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.SignupRequestDto;
import com.gbsb.routie_server.dto.LoginResponseDto;
import com.gbsb.routie_server.dto.LoginRequestDto;
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

    // 회원가입 DTO추가
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody SignupRequestDto signupDto) {
        try {
            User newUser = User.builder()
                    .userId(signupDto.getUserId())
                    .email(signupDto.getEmail())
                    .password(signupDto.getPassword())
                    .name(signupDto.getName())
                    .gold(0)
                    .isAdmin(false)
                    .build();

            userService.createUser(newUser);

            // JSON 응답 반환
            Map<String, String> response = new HashMap<>();
            response.put("message", "회원가입 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "회원가입 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // 로그인 DTO추가
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginDto) {
        try {
            // UserService 에서 로그인 검증
            User user = userService.loginUser(loginDto.getUserId(), loginDto.getPassword());

            // 로그인 성공 시 응답 DTO 생성
            LoginResponseDto response = new LoginResponseDto(
                    user.getUserId(),
                    user.getName(),
                    user.getGold(),
                    user.getTotalCaloriesBurned()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인 실패: " + e.getMessage());
        }
    }

    // 사용자 정보 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(userId, updatedUser);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("사용자 정보 수정 실패: " + e.getMessage());
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
