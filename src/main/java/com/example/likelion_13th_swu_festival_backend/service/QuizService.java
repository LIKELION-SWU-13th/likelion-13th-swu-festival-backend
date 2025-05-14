package com.example.likelion_13th_swu_festival_backend.service;

import com.example.likelion_13th_swu_festival_backend.dto.quizDTO.QuizResponseDto;
import com.example.likelion_13th_swu_festival_backend.repository.AnswerRepository;
import com.example.likelion_13th_swu_festival_backend.repository.QuizRepository;
import com.example.likelion_13th_swu_festival_backend.entity.Quiz;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final AnswerRepository answerRepository;

    public QuizResponseDto getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("quiz not found"));

        return QuizResponseDto.builder()
                .id(quiz.getId())
                .body(quiz.getBody())
                .a_body(quiz.getA_body())
                .b_body(quiz.getB_body())
                .status(quiz.getQuizStatus())
                .open_time(quiz.getOpen_time())
                .build();
    }

    public List<Long> getCompletedQuizzes(Long userId) {
        return answerRepository.findCompletedQuizIdsByUserId(userId);
    }

}

