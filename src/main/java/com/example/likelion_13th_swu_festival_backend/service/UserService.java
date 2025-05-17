package com.example.likelion_13th_swu_festival_backend.service;

import com.example.likelion_13th_swu_festival_backend.converter.UserConverter;
import com.example.likelion_13th_swu_festival_backend.dto.userDTO.UserRequestDTO;
import com.example.likelion_13th_swu_festival_backend.dto.userDTO.UserResponseDTO;
import com.example.likelion_13th_swu_festival_backend.entity.Answer;
import com.example.likelion_13th_swu_festival_backend.entity.User;
import com.example.likelion_13th_swu_festival_backend.jwt.JwtUtil;
import com.example.likelion_13th_swu_festival_backend.jwt.TokenStatus;
import com.example.likelion_13th_swu_festival_backend.repository.AnswerRepository;
import com.example.likelion_13th_swu_festival_backend.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final JwtUtil jwtUtil;

    @Value("${naver.ocr.general.url}")
    private String ocrUrl;

    @Value("${naver.ocr.secret}")
    private String ocrSecret;

    /*
     * MultipartFile 을 받아서 General OCR API 호출 후 응답 전체를 JsonNode 로 리턴
     */
    public JsonNode recognize(MultipartFile file) throws Exception {
        // 1) message JSON 만들기
        Map<String, Object> message = Map.of(
                "version",   "V2",
                "requestId", UUID.randomUUID().toString(),
                "timestamp", System.currentTimeMillis(),
                "lang",      "ko",
                "images", List.of(
                        Map.of(
                                "format", extractExtension(file.getOriginalFilename()),
                                "name",   "document"
                        )
                )
        );
        String messageJson = objectMapper.writeValueAsString(message);

        // 2) multipart body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // — message part (JSON)
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> jsonPart = new HttpEntity<>(messageJson, jsonHeaders);
        body.add("message", jsonPart);

        // — file part (bytes)
        ByteArrayResource imagePart = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("file", imagePart);

        // 3) 전체 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-OCR-SECRET", ocrSecret);

        HttpEntity<MultiValueMap<String,Object>> requestEntity =
                new HttpEntity<>(body, headers);

        // 4) 요청 전송
        ResponseEntity<String> response =
                restTemplate.postForEntity(ocrUrl, requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("OCR API 호출 실패: " + response.getStatusCode());
        }

        // 5) 응답 JSON 파싱
        return objectMapper.readTree(response.getBody());
    }

    /* 파일명에서 확장자(jpg, png)만 추출 */
    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    public Map<String, String> extractUserInfo(JsonNode ocrJson) {
        Map<String, String> info = new LinkedHashMap<>();
        JsonNode fields = ocrJson.path("images").path(0).path("fields");

        // OCR 결과 스캔하며 값 추출
        for (int i = 0; i < fields.size() - 1; i++) {
            String label = fields.get(i).path("inferText").asText().trim();
            String value = fields.get(i + 1).path("inferText").asText().trim();

            if (label.startsWith("아이디")) {
                info.put("아이디", value);
            } else if (label.startsWith("이름")) {
                info.put("이름", value);
            } else if (label.startsWith("소속")) {
                info.put("소속", value);
                // 소속까지 찾으면 루프 종료해도 되지만
                // 굳이 break하지 않아도 됩니다.
                break;
            }
        }

        // 필수 키(아이디, 이름, 소속)가 모두 있는지 검증
        List<String> required = List.of("아이디", "이름", "소속");
        if (!info.keySet().containsAll(required)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "OCR 결과에서 아이디, 이름, 소속 정보를 모두 찾을 수 없습니다."
            );
        }

        // 아이디 길이 검증: 10자리여야 함
        String userId = info.get("아이디");
        if (userId.length() != 10) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "아이디는 10자리여야 합니다."
            );
        }

        return info;
    }


    public UserResponseDTO.UserResultRsDTO createUser(UserRequestDTO.CreateUserRqDTO request){
        // 기존 사용자 확인
        User user = userRepository.findByStudentNum(request.getStudent_num());
        if (user == null) {
            // 없으면 신규 생성
            user = userRepository.save(UserConverter.createUser(request));
        }

        // access token 생성
        String accessToken = jwtUtil.generateAccessToken(user);

        // refresh token 생성 및 DB 저장
        String refreshToken = jwtUtil.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        userRepository.save(user); // 반드시 저장해서 서버가 관리

        return UserResponseDTO.UserResultRsDTO.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .user_id(user.getId())
                .build();

    }

    public TokenStatus validateRefreshToken(String token) {
        return jwtUtil.validateRefreshToken(token);
    }

    public String extractUsernameFromRefresh(String token) {
        return jwtUtil.extractUsernameFromRefresh(token);
    }

    public UserResponseDTO.TokenPairRsDTO reissueAccessToken(String studentNum, String requestRefreshToken) {
        User user = userRepository.findByStudentNum(studentNum);
        if (user == null || !user.getRefreshToken().equals(requestRefreshToken)) {
            throw new RuntimeException("Invalid refresh token or user not found");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user);
        return new UserResponseDTO.TokenPairRsDTO(newAccessToken, user.getRefreshToken());
    }


    public int determineUserType(Long userId) {

        int[] typeA = {1, 1, 1, 1, 3, 3, 1, 1, 3, 3, 1, 3}; // A일 때의 타입
        int[] typeB = {2, 3, 4, 4, 2, 2, 4, 4, 2, 4, 4, 2}; // B일 때의 타입

        List<Answer> userAnswers = answerRepository.findByUserIdOrderByQuizIdAsc(userId);

        if (userAnswers.size() != 12) {
            throw new IllegalStateException("아직 12개를 응답하지 않았습니다.");
        }

        // 타입별 카운팅 배열 (0:dummy, 1:🎤, 2:🌞, 3:🌿, 4:🔥)
        int[] counts = new int[5];

        for (int i = 0; i < 12; i++) {
            char choice = userAnswers.get(i).getChoice(); // 'A' or 'B'
            int type = (choice == 'A') ? typeA[i] : typeB[i];
            counts[type]++;
        }

        // 가장 많은 count를 가진 타입 찾기 (우선순위: 🎤 > 🌞 > 🌿 > 🔥)
        int maxType = 1;
        int maxCount = counts[1];

        for (int t = 2; t <= 4; t++) {
            if (counts[t] > maxCount || (counts[t] == maxCount && t < maxType)) {
                maxType = t;
                maxCount = counts[t];
            }
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재하지 않습니다."));
        user.setType(maxType);
        userRepository.save(user);

        return maxType;
    }


}
