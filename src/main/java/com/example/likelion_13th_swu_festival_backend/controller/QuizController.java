package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.dto.quizDTO.QuizPercentResponse;
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
import com.example.likelion_13th_swu_festival_backend.service.AnswerService;
import com.example.likelion_13th_swu_festival_backend.service.QuizAnswerService;
import com.example.likelion_13th_swu_festival_backend.service.QuizService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
@CrossOrigin(origins = "https://${frontend.domain}")
public class QuizController {

    private final QuizService quizService;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final JwtUtil jwtUtil;
    private final QuizAnswerService quizAnswerService;
    private final AnswerService answerService;


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
    /*@PostMapping("/{quizId}/answer")
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
    }*/

    @GetMapping("/{quiz_id}/percent")
    public ResponseEntity<?> getQuizPercent(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long quiz_id) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Long userId = userDetails.getUserId();
        Answer userAnswer = answerRepository.findByQuizIdAndUserId(quiz_id, userId);
        if (userAnswer == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "해당 퀴즈에 아직 응답하지 않았습니다."));
        }

        String userChoice = String.valueOf(userAnswer.getChoice());

        long totalCount = answerRepository.countByQuizId(quiz_id);
        long aCount = answerRepository.countByQuizIdAndChoice(quiz_id, 'A');
        long bCount = answerRepository.countByQuizIdAndChoice(quiz_id, 'B');

        double aPercent = totalCount == 0 ? 0 : (double) aCount / totalCount * 100;
        double bPercent = totalCount == 0 ? 0 : (double) bCount / totalCount * 100;

        QuizPercentResponse response = new QuizPercentResponse(userChoice, aPercent, bPercent);

        return ResponseEntity.ok(response);
    }


}
