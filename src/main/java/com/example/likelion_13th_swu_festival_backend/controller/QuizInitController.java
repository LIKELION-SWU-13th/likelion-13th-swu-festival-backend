package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.service.QuizInitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/init")
@RequiredArgsConstructor
public class QuizInitController {
    private final QuizInitService quizInitService;

    @PostMapping("/quizzes")
    public String insertQuizzes() {
        return quizInitService.insertInitialQuizzes();
    }
}
