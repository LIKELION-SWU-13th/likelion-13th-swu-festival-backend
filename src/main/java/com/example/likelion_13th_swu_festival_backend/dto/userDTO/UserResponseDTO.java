package com.example.likelion_13th_swu_festival_backend.dto.userDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDTO {
    //사용자 생성 후 토큰, id 반환
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResultRsDTO{
        String access_token;
        String refresh_token;
        Long user_id;
    }
}