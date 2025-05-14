package com.example.likelion_13th_festival_BE.dto.boothDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoothStatusResponse {
    private String name;       // 부스 이름
    private boolean isActive;  // 부스 운영 상태
}
