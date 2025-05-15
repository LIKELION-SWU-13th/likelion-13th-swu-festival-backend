package com.example.likelion_13th_swu_festival_backend.service;

import com.example.likelion_13th_swu_festival_backend.entity.Quiz;
import com.example.likelion_13th_swu_festival_backend.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizInitService {

    private final QuizRepository quizRepository;

    public String insertInitialQuizzes() {
        if (quizRepository.count() > 0) {
            return "퀴즈 데이터가 이미 존재합니다.";
        }

        Quiz quiz1 = new Quiz();
        quiz1.setBody("공연을 볼 때 선호하는 자리는?");
        quiz1.setA_body("무대 앞 1열! 스탠딩이 좋아요");
        quiz1.setB_body("뒤쪽 그늘에서 여유롭게 즐기고 싶어요.");

        Quiz quiz2 = new Quiz();
        quiz2.setBody("축제에 가면 슈니가 가장 먼저 가는 곳은?");
        quiz2.setA_body("메인 무대! 공연부터 즐겨요.");
        quiz2.setB_body("금강산도 식후경! 푸드트럭부터 가요.");

        Quiz quiz3 = new Quiz();
        quiz3.setBody("푸드트럭 구역에서의 슈니는?");
        quiz3.setA_body("계획해 온 맛집부터");
        quiz3.setB_body("보이는 대로 즉흥 선택");

        Quiz quiz4 = new Quiz();
        quiz4.setBody("친구들과의 이동 스타일은?");
        quiz4.setA_body("시간 맞춰 이동! 일정대로 움직여요.");
        quiz4.setB_body("걷다가 분위기 따라 즉흥적으로 이동!");

        Quiz quiz5 = new Quiz();
        quiz5.setBody("인생샷은 언제 찍는 게 좋을까?");
        quiz5.setA_body("메인 무대 앞, 분위기 한가득일 때!");
        quiz5.setB_body("해질 무렵 감성 가득한 풍경에서");

        Quiz quiz6 = new Quiz();
        quiz6.setBody("축제 브이로그를 찍는다면?");
        quiz6.setA_body("공연 중심으로 촬영할래요!");
        quiz6.setB_body("이곳저곳 분위기 중심으로 담을래요!");

        Quiz quiz7 = new Quiz();
        quiz7.setBody("줄이 길다면?");
        quiz7.setA_body("기다려서라도 내가 찜한 곳!");
        quiz7.setB_body("줄 없는 곳으로 방향 전환!");

        Quiz quiz8 = new Quiz();
        quiz8.setBody("굿즈를 구매할 때는?");
        quiz8.setA_body("필수템! 오픈런해서라도 겟");
        quiz8.setB_body("보다가 예쁜 게 있으면 그때 사요");

        Quiz quiz9 = new Quiz();
        quiz9.setBody("피크닉존에서의 나?");
        quiz9.setA_body("돗자리 깔고 여유롭게 힐링!");
        quiz9.setB_body("잠깐 쉬고 또 다른 존으로 출발~");

        Quiz quiz10 = new Quiz();
        quiz10.setBody("나의 축제 OOTD는?");
        quiz10.setA_body("컨셉 맞춰 미리 코디 완성");
        quiz10.setB_body("편하고 시원한 게 최고!");

        Quiz quiz11 = new Quiz();
        quiz11.setBody("가장 기대되는 순간은?");
        quiz11.setA_body("하이라이트 공연 시간!");
        quiz11.setB_body("전체 분위기를 느끼는 순간");

        Quiz quiz12 = new Quiz();
        quiz12.setBody("오늘 축제, 끝나고 나면?");
        quiz12.setA_body("하이라이트 영상 편집해 올려야지!");
        quiz12.setB_body("추억은 마음에 저장 :)");

        quizRepository.saveAll(List.of(quiz1, quiz2, quiz3, quiz4,quiz5, quiz6, quiz7, quiz8, quiz9, quiz10, quiz11, quiz12));
        return "초기 퀴즈 데이터 삽입 완료!";
    }

}
