package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booth")
public class BoothController {

    private final com.example.likelion_13th_swu_festival_backend.service.BoothService boothService;

    @GetMapping("/info")
    public ResponseEntity<com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothInfoResponse> getBoothInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothInfoResponse response = boothService.getBoothInfo(userId);
        return ResponseEntity.ok(response);
    }

}
