package com.example.likelion_13th_swu_festival_backend.service;

import com.example.likelion_13th_swu_festival_backend.aop.RedissonLock;
import com.example.likelion_13th_swu_festival_backend.dto.answerDTO.AnswerReturnDto;
import com.example.likelion_13th_swu_festival_backend.entity.Answer;
import com.example.likelion_13th_swu_festival_backend.entity.Quiz;
import com.example.likelion_13th_swu_festival_backend.entity.User;
import com.example.likelion_13th_swu_festival_backend.repository.AnswerRepository;
import com.example.likelion_13th_swu_festival_backend.repository.QuizRepository;
import com.example.likelion_13th_swu_festival_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final CouponService couponService;

    @RedissonLock(value = "#quiz_id", waitTime = 2000, leaseTime = 3000)
    public boolean saveAnswer(Long userId, char choice, Long quiz_id) throws Exception{
        // dto에 모든 값이 잘 들어있는지 확인: 없으면 오류 반환
        if(quiz_id == null || choice  == '\u0000') {
            throw new IllegalArgumentException("quiz_id or body is null quiz_id: " + quiz_id + "/ choice: " + choice);
        }

        // 이 퀴즈를 푼 적 있는지 검사: 있으면 에러 반환
        Answer findAnswer = answerRepository.findByQuizIdAndUserId(quiz_id, userId);
        if(findAnswer != null) {
            throw new IllegalStateException("answer is already exists");
        }


        Optional<Quiz> optionalQuiz = quizRepository.findById(quiz_id);

        if (optionalQuiz.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "quiz_id is null");
        }


        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("quiz_id or body is null quiz_id: " + quiz_id + "/ choice: " + choice);
        }

        Quiz quiz = optionalQuiz.get();

        // quiz 선착순 남은 쿠폰 갯수 확인
        Long count = quiz.getCount();

        // 만약 남았다면 isWin을 true로 아니면 false로 설정
        boolean isWin = (count > 0) ? true : false;

        // answer 객체 생성
        Answer answer = Answer.builder()
                .quiz(quiz)
                .choice(choice)
                .user(optionalUser.get())
                .isWin(isWin)
                .build();

        // 저장
        answerRepository.save(answer);

        if (isWin) {
            // 발급 가능한 쿠폰 갯수 감소
            quiz.decreaseQuizCount();
            quizRepository.saveAndFlush(quiz);

        }

        // isWin 값 반환
        return isWin;
    }



    // 투표율 계산 메서드
    public AnswerReturnDto calculateVoterTurnout(Long quiz_id, boolean isWin, char choice) {
        Long countA = answerRepository.countByQuizIdAndChoice(quiz_id, 'A');
        Long countB = answerRepository.countByQuizIdAndChoice(quiz_id, 'B');

        Long total = countA + countB;

        AnswerReturnDto answerReturnDto = new AnswerReturnDto();
        answerReturnDto.set_win(isWin);
        answerReturnDto.setChoice(choice);
        answerReturnDto.setA_rate(((float)countA / total) * 100);
        answerReturnDto.setB_rate(((float)countB / total) * 100);

        return answerReturnDto;
    }




}
