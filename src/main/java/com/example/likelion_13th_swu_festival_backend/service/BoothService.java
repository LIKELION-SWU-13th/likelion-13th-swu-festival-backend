package com.example.likelion_13th_swu_festival_backend.service;

import com.example.likelion_13th_swu_festival_backend.repository.BoothRepository;
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



    public BoothInfoResponse getBoothInfo(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        List<String> boothNames = boothRepository.findAll().stream()
                .map(Booth::getName)
                .collect(Collectors.toList());

        return new BoothInfoResponse(boothNames, user.getMajor());
    }
}
