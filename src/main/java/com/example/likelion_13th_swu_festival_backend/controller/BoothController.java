package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothStatusResponse;
import com.example.likelion_13th_swu_festival_backend.security.CustomUserDetails;
import com.example.likelion_13th_swu_festival_backend.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booth")
public class BoothController {

    private final BoothService boothService;

    @GetMapping("/info")
    public ResponseEntity<com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothInfoResponse> getBoothInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothInfoResponse response = boothService.getBoothInfo(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{boothId}/info")
    public ResponseEntity<BoothStatusResponse> getBoothStatus(@PathVariable Long boothId) {
        BoothStatusResponse response = boothService.getBoothStatus(boothId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{boothId}/participate")
    public ResponseEntity<Void> participateBooth(@PathVariable Long boothId,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        boothService.participateBooth(userDetails.getUserId(), boothId);
        return ResponseEntity.ok().build();
    }

}
