package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 모든 사용자 조회 (GET /api/admin/users)
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 이메일을 조회 (GET /api/admin/user?email=example@example.com)
    @GetMapping("/user")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        Optional<User> userOpt = adminService.getUserByEmail(email);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(404)
                    .body("해당 이메일의 사용자가 존재하지 않습니다.");
        }
    }
}
