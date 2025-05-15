package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.entity.Booth;
import com.example.likelion_13th_swu_festival_backend.repository.BoothRepository;
import com.example.likelion_13th_swu_festival_backend.service.InitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/init")
@RequiredArgsConstructor
public class InitController {

    private final InitService quizInitService;
    private final BoothRepository boothRepository;


    @PostMapping("/quizzes")
    public String insertQuizzes() {
        return quizInitService.insertInitialQuizzes();
    }

    @PostMapping("/booths")
    public ResponseEntity<String> initBoothData() {
        List<Booth> booths = List.of(
                new Booth("디지털미디어학과", true),
                new Booth("미래산업융합대학", true),
                new Booth("산업디자인학과", true),
                new Booth("소프트웨어융합학과", true),
                new Booth("정보보호학과", true),
                new Booth("데이터사이언스학과", true),
                new Booth("경영학과", true),
                new Booth("국어국문학과", true),
                new Booth("식품영양학과", true),
                new Booth("미래산업융합대학", true),
                new Booth("산업디자인학과", true),
                new Booth("소프트웨어융합학과", true),
                new Booth("정보보호학과", true),
                new Booth("데이터사이언스학과", true),
                new Booth("경영학과", true),
                new Booth("국어국문학과", true),
                new Booth("디지털미디어학과", true),
                new Booth("화학과", true)
        );
        boothRepository.saveAll(booths);
        return ResponseEntity.ok("부스 데이터 초기화 완료!");
    }
}
