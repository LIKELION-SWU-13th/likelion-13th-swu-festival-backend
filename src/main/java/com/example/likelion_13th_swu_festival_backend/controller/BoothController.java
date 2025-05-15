package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothStatusResponse;
import com.example.likelion_13th_swu_festival_backend.security.CustomUserDetails;
import com.example.likelion_13th_swu_festival_backend.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booth")
@CrossOrigin(origins = "https://${frontend.domain}")
public class BoothController {

    private final BoothService boothService;

    @GetMapping("/info")
    public ResponseEntity<com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothInfoResponse> getBoothInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothInfoResponse response = boothService.getBoothInfo(userId);
        return ResponseEntity.ok(response);
    }

    /*
    @GetMapping("/{boothId}/info")
    public ResponseEntity<BoothStatusResponse> getBoothStatus(@PathVariable Long boothId) {
        BoothStatusResponse response = boothService.getBoothStatus(boothId);
        return ResponseEntity.ok(response);
    }
    */

    @PostMapping("/{boothId}/participate")
    public ResponseEntity<String> participateBooth(@PathVariable Long boothId,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        boothService.participateBooth(userDetails.getUserId(), boothId);
        return ResponseEntity.ok("참여가 완료되었습니다.");
    }

    /*
    @GetMapping("/{boothId}/complete")
    public ResponseEntity<Map<String, Boolean>> isBoothCompleted(@PathVariable Long boothId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean participated = boothService.hasParticipated(userDetails.getUserId(), boothId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("isParticipated", participated);
        return ResponseEntity.ok(result);
    }
    */

    @GetMapping("/complete")
    public ResponseEntity<List<Long>> getParticipatedBooths(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<Long> boothIdList = boothService.getParticipatedBooths(userDetails.getUserId());
        return ResponseEntity.ok(boothIdList);
    }


}
