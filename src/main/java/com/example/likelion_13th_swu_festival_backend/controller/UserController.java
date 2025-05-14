package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.dto.userDTO.UserRequestDTO;
import com.example.likelion_13th_swu_festival_backend.dto.userDTO.UserResponseDTO;
import com.example.likelion_13th_swu_festival_backend.security.CustomUserDetails;
import com.example.likelion_13th_swu_festival_backend.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
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

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO.UserResultRsDTO> createUser(
            @RequestBody @Valid UserRequestDTO.CreateUserRqDTO request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/auth")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok("JWT 인증 성공");
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> myPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        String studentNum = userDetails.getStudentNum();

        return ResponseEntity.ok("userId: " + userId + ", studentNum: " + studentNum);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("1");
    }


}
