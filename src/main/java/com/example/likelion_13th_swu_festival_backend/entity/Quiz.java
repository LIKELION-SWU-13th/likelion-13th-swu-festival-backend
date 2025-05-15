package com.example.likelion_13th_swu_festival_backend.entity;

import com.example.likelion_13th_swu_festival_backend.constant.QuizStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter
@Entity
public class Quiz {
    @Id
    @Column(name = "quiz_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    private String a_body;

    private String b_body;

    @Enumerated(EnumType.STRING)
    private QuizStatus quizStatus;

    private LocalDateTime open_time;

    // 남은 쿠폰 발급 갯수
    @ColumnDefault("2")
    private Long count;

    public void decreaseQuizCount() {
        -- this.count;
    }

}
