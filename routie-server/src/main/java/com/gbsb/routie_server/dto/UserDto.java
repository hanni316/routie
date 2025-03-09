package com.gbsb.routie_server.dto;

import com.gbsb.routie_server.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;
    private String email;
    private String name;
    private int gold;
    //private boolean isAdmin;
    private double totalCaloriesBurned;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.gold = user.getGold();
        //this.isAdmin = user.isAdmin();
        this.totalCaloriesBurned = user.getTotalCaloriesBurned();
    }
}
