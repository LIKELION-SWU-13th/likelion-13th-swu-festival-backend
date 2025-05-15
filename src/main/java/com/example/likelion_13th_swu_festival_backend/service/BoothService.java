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

        List<String> boothNames = boothRepository.findAll().stream()
                .map(Booth::getName)
                .collect(Collectors.toList());

        return new BoothInfoResponse(boothNames, user.getMajor());
    }

    public BoothStatusResponse getBoothStatus(Long boothId) {
        // boothId로 부스를 조회
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new RuntimeException("Booth not found with id: " + boothId));

        // 부스 이름과 운영 상태를 BoothStatusResponse에 담아서 반환
        return new BoothStatusResponse(booth.getName(), booth.getIsActive());
    }

    public void participateBooth(Long userId, Long boothId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new RuntimeException("Booth not found"));

        if (userBoothRepository.existsByUserAndBooth(user, booth)) {
            throw new IllegalStateException("Already participated in this booth.");
        }

        UserBooth userBooth = UserBooth.builder()
                .user(user)
                .booth(booth)
                .build();

        userBoothRepository.save(userBooth);
    }
}
