package com.gbsb.routie_server.dto;

public interface UserRankingProjection {
    String getNickname();
    String getProfileImageUrl();
    Double getTotal();
    String getUserId(); // 내 랭킹 계산용
}
