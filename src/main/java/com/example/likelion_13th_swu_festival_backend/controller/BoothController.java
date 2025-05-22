package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothInfoResponse;
import com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothStatusResponse;
import com.example.likelion_13th_swu_festival_backend.security.CustomUserDetails;
import com.example.likelion_13th_swu_festival_backend.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getBoothInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long userId = userDetails.getUserId();
            BoothInfoResponse response = boothService.getBoothInfo(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }



    /*
    @GetMapping("/{boothId}/info")
    public ResponseEntity<BoothStatusResponse> getBoothStatus(@PathVariable Long boothId) {
        BoothStatusResponse response = boothService.getBoothStatus(boothId);
        return ResponseEntity.ok(response);
    }
    */

    /*
    @PostMapping("/{boothId}/participate")
    public ResponseEntity<String> participateBooth(@PathVariable Long boothId,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        boothService.participateBooth(userDetails.getUserId(), boothId);
        return ResponseEntity.ok("참여가 완료되었습니다.");
    }*/

    @PostMapping("/{boothId}/participate")
    public ResponseEntity<String> participateBooth(@PathVariable Long boothId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long dbBoothId = boothService.convertToDatabaseBoothId(boothId);  // ★ 프론트 → DB 번호 변환

        boothService.participateBooth(userDetails.getUserId(), dbBoothId);
        return ResponseEntity.ok("참여가 완료되었습니다.");
    }


    @GetMapping("/complete")
    public ResponseEntity<List<Long>> getParticipatedBooths(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Long> dbBoothIdList = boothService.getParticipatedBooths(userDetails.getUserId());
        List<Long> frontBoothIdList = boothService.convertDbBoothIdListToFront(dbBoothIdList);
        return ResponseEntity.ok(frontBoothIdList);
    }


}
