package com.example.likelion_13th_swu_festival_backend.repository;

import com.example.likelion_13th_swu_festival_backend.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 사용자 ID로 완료된 퀴즈들의 ID 목록을 반환
    @Query("SELECT a.quiz.id FROM Answer a WHERE a.user.id = :userId")
    List<Long> findCompletedQuizIdsByUserId(Long userId);

    // 퀴즈 ID에 따른 모든 응답 조회
    List<Answer> findByQuizId(Long quizId);

}
