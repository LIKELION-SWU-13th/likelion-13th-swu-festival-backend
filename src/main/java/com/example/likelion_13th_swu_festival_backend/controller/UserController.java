package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.dto.userDTO.UserRequestDTO;
import com.example.likelion_13th_swu_festival_backend.dto.userDTO.UserResponseDTO;
import com.example.likelion_13th_swu_festival_backend.jwt.TokenStatus;
import com.example.likelion_13th_swu_festival_backend.security.CustomUserDetails;
import com.example.likelion_13th_swu_festival_backend.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://${frontend.domain}")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,String>> generalOcr(@RequestParam("file") MultipartFile file) throws Exception {
        // 1) OCR 전체 JSON
        JsonNode ocrResult = userService.recognize(file);

        // 2) 필요한 부분만 잘라내기
        Map<String,String> userInfo = userService.extractUserInfo(ocrResult);

        return ResponseEntity.ok(userInfo);
    }

    // 리프레시 토큰이 만료되었을 때 사용하는 api
    // 리프레시 토큰 재발급
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO.UserResultRsDTO> createUser(
            @RequestBody @Valid UserRequestDTO.CreateUserRqDTO request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/auth")
    public ResponseEntity<?> testApi(@RequestHeader("Authorization") String refreshToken) {
        // refresh Token 유효성 검증
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        if (userService.validateRefreshToken(refreshToken) != TokenStatus.AUTHENTICATED) {
            return ResponseEntity.status(401).body("유효하지 않거나 만료된 리프레시 토큰입니다");
        }
        return ResponseEntity.ok("refresh token 인증 성공");
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> myPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        String studentNum = userDetails.getStudentNum();

        return ResponseEntity.ok("userId: " + userId + ", studentNum: " + studentNum);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        // refresh Token 유효성 검증
        if (userService.validateRefreshToken(refreshToken) != TokenStatus.AUTHENTICATED) {
            return ResponseEntity.status(401).body("유효하지 않거나 만료된 리프레시 토큰입니다");
        }

        // 토큰에서 studentNum 추출
        String studentNum = userService.extractUsernameFromRefresh(refreshToken);

        // 사용자 조회 및 저장된 refresh token과 일치 확인 + 새 Access Token 발급
        UserResponseDTO.TokenPairRsDTO tokenPairRsDTO = userService.reissueAccessToken(studentNum, refreshToken);

        return ResponseEntity.ok(tokenPairRsDTO);
    }

    @GetMapping("/type")
    public ResponseEntity<Map<String, Integer>> getUserType(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Long userId = userDetails.getUserId();
        int type = userService.determineUserType(userId);

        Map<String, Integer> response = new HashMap<>();
        response.put("type", type);

        return ResponseEntity.ok(response);
    }


}
