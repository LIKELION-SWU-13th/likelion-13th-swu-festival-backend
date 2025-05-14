package com.example.likelion_13th_swu_festival_backend.dto.userDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDTO {
    // 리프레시 토큰 만료될 시 반환
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResultRsDTO{
        String access_token;
        String refresh_token;
        Long user_id;
    }

    @Getter
    @AllArgsConstructor
    public static class TokenPairRsDTO {
        private String accessToken;
        private String refreshToken;
    }
}