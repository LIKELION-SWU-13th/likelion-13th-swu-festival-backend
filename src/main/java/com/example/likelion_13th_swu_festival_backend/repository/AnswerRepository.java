package com.example.likelion_13th_swu_festival_backend.repository;

import com.example.likelion_13th_swu_festival_backend.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 사용자 ID로 완료된 퀴즈들의 ID 목록을 반환
    @Query("SELECT a.quiz.id FROM Answer a WHERE a.user.id = :userId")
    List<Long> findCompletedQuizIdsByUserId(Long userId);

    // 퀴즈 ID에 따른 모든 응답 조회
    List<Answer> findByQuizId(Long quizId);

    //해당 퀴즈에 대한 응답 개수를 카운트
    Long countByQuizId(Long quizId);

    // 퀴즈 id와 유저 id로 응답 조회
    public Answer findByQuizIdAndUserId(Long quiz_id, Long user_id);

    // 퀴즈 id와 응답으로 객체 갯수 조회
    public Long countByQuizIdAndChoice(Long quizId, char choice);

    // 유저 id와 퀴즈 id로 당첨 기록이 있는 객체가 있는지 반환
    public boolean existsByUserIdAndQuizId(Long userId, Long quizId);

    Optional<Answer> findOptionalByQuizIdAndUserId(Long quiz_id, Long user_id);

    List<Answer> findByUserIdOrderByQuizIdAsc(Long userId);


}
