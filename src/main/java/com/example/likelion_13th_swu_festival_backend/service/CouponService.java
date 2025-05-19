package com.example.likelion_13th_swu_festival_backend.service;

import com.example.likelion_13th_swu_festival_backend.aop.RedissonLock;
import com.example.likelion_13th_swu_festival_backend.entity.Quiz;
import com.example.likelion_13th_swu_festival_backend.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final QuizRepository quizRepository;

    @RedissonLock(value = "#quiz.id")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean tryWinningCoupon(Quiz quiz) {
        if (quiz.getCount() > 0) {
            quiz.decreaseQuizCount();
            quizRepository.saveAndFlush(quiz);
            return true;
        }
        return false;
    }
}

