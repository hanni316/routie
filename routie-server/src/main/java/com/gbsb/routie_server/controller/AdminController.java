package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.repository.UserRepository;
import com.gbsb.routie_server.service.AdminService;
import com.gbsb.routie_server.service.RankingRewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final RankingRewardService rankingRewardService;
    private final UserRepository userRepository;

    public AdminController(AdminService adminService, RankingRewardService rankingRewardService, UserRepository userRepository) {
        this.adminService = adminService;
        this.rankingRewardService = rankingRewardService;
        this.userRepository = userRepository;
    }

    // 모든 사용자 조회
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 사용자 ID로 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        return adminService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 관리자만 수동으로 랭킹 보상 지급
    @PostMapping("/trigger-reward/{userId}")
    public ResponseEntity<String> triggerRewardManually(@PathVariable String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("해당 ID의 사용자가 존재하지 않습니다.");
        }

        User user = optionalUser.get();

        if (!user.isAdmin()) {
            return ResponseEntity.status(403).body("접근 권한이 없습니다. 관리자만 실행할 수 있습니다.");
        }

        rankingRewardService.distributeRankingRewards();
        return ResponseEntity.ok("관리자 권한으로 주간 랭킹 보상이 성공적으로 지급되었습니다.");
    }
}
