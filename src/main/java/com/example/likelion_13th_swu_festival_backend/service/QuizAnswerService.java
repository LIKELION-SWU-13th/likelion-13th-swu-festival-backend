package com.example.likelion_13th_swu_festival_backend.service;

import com.example.likelion_13th_swu_festival_backend.repository.AnswerRepository;
import com.example.likelion_13th_swu_festival_backend.repository.QuizRepository;
import com.example.likelion_13th_swu_festival_backend.dto.quizDTO.QuizAnswerResponseDto;
import com.example.likelion_13th_swu_festival_backend.entity.Answer;
import com.example.likelion_13th_swu_festival_backend.entity.Quiz;
import com.example.likelion_13th_swu_festival_backend.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizAnswerService {

    private final QuizRepository quizRepository;
    private final AnswerRepository answerRepository;

    public QuizAnswerService(QuizRepository quizRepository, AnswerRepository answerRepository) {
        this.quizRepository = quizRepository;
        this.answerRepository = answerRepository ;
    }

    public QuizAnswerResponseDto processQuizAnswer(User user, Long quizId, String choice) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        List<Answer> answers = answerRepository.findByQuizId(quizId);

        // 응답 수 계산
        long totalAnswers = answers.size();
        long countA = answers.stream().filter(answer -> answer.getChoice() == 'A').count();

        // 확률 계산
        double aRate = (totalAnswers > 0) ? ((double) countA / totalAnswers) * 100 : 0;
        double bRate = 100 - aRate;

        boolean isWin = false;
        // 응답 DTO 반환
        QuizAnswerResponseDto responseDto = new QuizAnswerResponseDto();
        responseDto.setChoice(choice);
        responseDto.setIsWin(isWin);
        responseDto.setARate(aRate);
        responseDto.setBRate(bRate);

        return responseDto;
    }
}
