package com.example.likelion_13th_swu_festival_backend.service;

import com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothStatusResponse;
import com.example.likelion_13th_swu_festival_backend.entity.UserBooth;
import com.example.likelion_13th_swu_festival_backend.repository.BoothRepository;
import com.example.likelion_13th_swu_festival_backend.repository.UserBoothRepository;
import com.example.likelion_13th_swu_festival_backend.repository.UserRepository;
import com.example.likelion_13th_swu_festival_backend.dto.boothDTO.BoothInfoResponse;
import com.example.likelion_13th_swu_festival_backend.entity.Booth;
import com.example.likelion_13th_swu_festival_backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothService {


    private final BoothRepository boothRepository;
    private final UserRepository userRepository;
    private final UserBoothRepository userBoothRepository;

    private static final Map<Long, Long> boothNumberMap = Map.ofEntries(
            Map.entry(1L, 29L),
            Map.entry(2L, 30L),
            Map.entry(3L, 31L),
            Map.entry(4L, 32L),
            Map.entry(5L, 33L),
            Map.entry(6L, 34L),
            Map.entry(7L, 35L),
            Map.entry(8L, 36L),
            Map.entry(9L, 37L),
            Map.entry(10L, 38L),
            Map.entry(11L, 39L),
            Map.entry(12L, 40L),
            Map.entry(13L, 41L),
            Map.entry(14L, 42L),
            Map.entry(15L, 43L),
            Map.entry(16L, 44L),
            Map.entry(17L, 45L),
            Map.entry(18L, 46L),
            Map.entry(19L, 47L),
            Map.entry(20L, 48L),
            Map.entry(21L, 49L),
            Map.entry(22L, 50L),
            Map.entry(23L, 51L),
            Map.entry(24L, 52L),
            Map.entry(25L, 53L),
            Map.entry(26L, 55L),
            Map.entry(27L, 56L),
            Map.entry(28L, 57L),
            Map.entry(29L, 58L),
            Map.entry(30L, 59L),
            Map.entry(31L, 60L),
            Map.entry(32L, 61L),
            Map.entry(33L, 62L),
            Map.entry(34L, 63L),
            Map.entry(35L, 64L)
    );

    private static final Map<Long, Long> dbToFrontMap = boothNumberMap.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));


    // 프론트에서 받은 부스 번호 → DB 부스 번호로 변환
    public Long convertToDatabaseBoothId(Long frontBoothId) {
        return boothNumberMap.getOrDefault(frontBoothId, frontBoothId);
    }

    // DB 부스 번호 → 프론트 부스 번호로 변환
    public Long convertToFrontBoothId(Long dbBoothId) {
        return dbToFrontMap.getOrDefault(dbBoothId, dbBoothId);
    }

    public List<Long> convertDbBoothIdListToFront(List<Long> dbBoothIdList) {
        return dbBoothIdList.stream()
                .map(this::convertToFrontBoothId)
                .collect(Collectors.toList());
    }

    public BoothInfoResponse getBoothInfo(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        int startId, endId;
        switch (dayOfWeek) {
            case THURSDAY:
                startId = 29; //29
                endId = 64; //64
                break;
            case FRIDAY:
                startId = 65; //65
                endId = 97;  //97
                break;
            case WEDNESDAY:
            default:
                startId = 29; //1
                endId = 64; //28
                break;
        }

        List<Booth> booths = boothRepository.findByIdBetween((long) startId, (long) endId);

        booths.forEach(b -> System.out.println("[BoothService] Booth id: " + b.getId() + ", name: " + b.getName()));

        List<String> boothNames = booths.stream()
                .map(Booth::getName)
                .collect(Collectors.toList());

        return new BoothInfoResponse(boothNames, user.getMajor());
    }


    /*
    public BoothStatusResponse getBoothStatus(Long boothId) {
        // boothId로 부스를 조회
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new RuntimeException("Booth not found with id: " + boothId));

        // 부스 이름과 운영 상태를 BoothStatusResponse에 담아서 반환
        return new BoothStatusResponse(booth.getName(), booth.getIsActive());
    }*/

    public void participateBooth(Long userId, Long boothId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booth targetBooth = boothRepository.findById(boothId)
                .orElseThrow(() -> new RuntimeException("Booth not found"));

        // 1. 같은 이름의 부스 전부 조회
        List<Booth> sameNamedBooths = boothRepository.findByName(targetBooth.getName());

        // 2. 같은 이름의 부스에 대해 참여 처리
        for (Booth booth : sameNamedBooths) {
            if (!userBoothRepository.existsByUserAndBooth(user, booth)) {
                UserBooth userBooth = UserBooth.builder()
                        .user(user)
                        .booth(booth)
                        .build();
                userBoothRepository.save(userBooth);
            }
        }
    }
    /*
    public boolean hasParticipated(Long userId, Long boothId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new RuntimeException("Booth not found"));

        return userBoothRepository.existsByUserAndBooth(user, booth);
    }
     */

    public List<Long> getParticipatedBooths(Long userId) {
        List<UserBooth> userBooths = userBoothRepository.findAllByUserId(userId);

        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        long startId, endId;
        switch (dayOfWeek) { //1-28, 29-64, 65-97
            case THURSDAY:
                startId = 29;
                endId = 64;
                break;
            case FRIDAY:
                startId = 65;
                endId = 97;
                break;
            case WEDNESDAY:
            default:
                startId = 29;
                endId = 64;
                break;
        }

        return userBooths.stream()
                .map(userBooth -> userBooth.getBooth().getId())
                .filter(id -> id >= startId && id <= endId)
                .collect(Collectors.toList());
    }

}
