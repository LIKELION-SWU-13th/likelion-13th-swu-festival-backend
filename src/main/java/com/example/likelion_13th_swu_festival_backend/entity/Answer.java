package com.example.likelion_13th_swu_festival_backend.entity;

import com.example.likelion_13th_swu_festival_backend.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Answer extends BaseEntity {
    @Id
    @Column(name = "answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private char choice;

    private boolean isWin;

    @Builder
    public  Answer(Quiz quiz, User user, char choice, boolean isWin) {
        this.quiz = quiz;
        this.user = user;
        this.choice = choice;
        this.isWin = isWin;
    }

    public void setIsWin(boolean isWin) {
        this.isWin = isWin;
    }


}
