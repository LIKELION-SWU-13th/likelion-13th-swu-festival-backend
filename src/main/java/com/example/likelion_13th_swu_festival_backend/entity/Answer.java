package com.example.likelion_13th_swu_festival_backend.entity;

import jakarta.persistence.*;

@Entity
public class Answer {
    @Id
    @Column(name = "answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private char choice;

    private boolean is_win;

}
