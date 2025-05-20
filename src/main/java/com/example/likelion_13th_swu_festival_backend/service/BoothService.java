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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;
    private final UserRepository userRepository;
    private final UserBoothRepository userBoothRepository;

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
                startId = 1; //1
                endId = 28; //28
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
        switch (dayOfWeek) { //1-18, 29-64, 65-97
            case THURSDAY:
                startId = 1;
                endId = 28;
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
