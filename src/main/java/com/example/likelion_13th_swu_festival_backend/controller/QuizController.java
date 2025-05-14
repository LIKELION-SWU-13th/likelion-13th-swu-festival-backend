package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.repository.AnswerRepository;
import com.example.likelion_13th_swu_festival_backend.repository.QuizRepository;
import com.example.likelion_13th_swu_festival_backend.repository.UserRepository;
import com.example.likelion_13th_swu_festival_backend.dto.quizDTO.QuizAnswerResponseDto;
import com.example.likelion_13th_swu_festival_backend.dto.quizDTO.QuizResponseDto;
import com.example.likelion_13th_swu_festival_backend.entity.Answer;
import com.example.likelion_13th_swu_festival_backend.entity.Quiz;
import com.example.likelion_13th_swu_festival_backend.entity.User;
import com.example.likelion_13th_swu_festival_backend.jwt.JwtUtil;
import com.example.likelion_13th_swu_festival_backend.security.CustomUserDetails;
import com.example.likelion_13th_swu_festival_backend.service.QuizAnswerService;
import com.example.likelion_13th_swu_festival_backend.service.QuizService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final JwtUtil jwtUtil;
    private final QuizAnswerService quizAnswerService;


    @GetMapping("/{quizId}")
    public ResponseEntity<?> getQuiz(@PathVariable Long quizId) {
        try {
            QuizResponseDto quiz = quizService.getQuizById(quizId);
            return ResponseEntity.ok(quiz);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "quiz not found"));
        }
    }

    // 완료된 퀴즈 응답 리스트 반환
    @GetMapping("/star")
    public ResponseEntity<List<Long>> getCompletedQuizzes(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();

        List<Long> completedQuizzes = quizService.getCompletedQuizzes(userId);

        return ResponseEntity.ok(completedQuizzes);
    }

    // 해당 quizId에 응답 저장
    @PostMapping("/{quizId}/answer")
    public ResponseEntity<?> submitQuizAnswer(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable Long quizId,
                                              @RequestBody Map<String, String> body) {
        User user = userRepository.findById(userDetails.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }

        String choice = body.get("choice");

        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Quiz not found"));
        }

        Answer answer = new Answer();
        answer.setUser(user);
        answer.setQuiz(quiz);
        answer.setChoice(choice.charAt(0));

        answer.setIsWin(false);

        answerRepository.save(answer);

        try {
            QuizAnswerResponseDto response = quizAnswerService.processQuizAnswer(user, quizId, choice);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Quiz not found"));
        }
    }

}
