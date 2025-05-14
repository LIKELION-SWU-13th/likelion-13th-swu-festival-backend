package com.example.likelion_13th_swu_festival_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
public class Answer {
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

    private boolean is_win;

    public void setIsWin(boolean isWin) {
        this.is_win = isWin;
    }


}
