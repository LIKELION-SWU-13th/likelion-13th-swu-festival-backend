package com.example.likelion_13th_swu_festival_backend.service;

import com.example.likelion_13th_swu_festival_backend.dto.quizDTO.QuizResponseDto;
import com.example.likelion_13th_swu_festival_backend.entity.Answer;
import com.example.likelion_13th_swu_festival_backend.repository.AnswerRepository;
import com.example.likelion_13th_swu_festival_backend.repository.QuizRepository;
import com.example.likelion_13th_swu_festival_backend.entity.Quiz;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Double> getVoteRates(Long quizId) {
        List<Answer> answers = answerRepository.findByQuizId(quizId);

        long totalAnswers = answers.size();
        long countA = answers.stream().filter(answer -> answer.getChoice() == 'A').count();

        double aRate = totalAnswers > 0 ? (double) countA / totalAnswers * 100 : 0;
        double bRate = 100 - aRate;

        Map<String, Double> rates = new HashMap<>();
        rates.put("a_rate", aRate);
        rates.put("b_rate", bRate);

        return rates;
    }

}

