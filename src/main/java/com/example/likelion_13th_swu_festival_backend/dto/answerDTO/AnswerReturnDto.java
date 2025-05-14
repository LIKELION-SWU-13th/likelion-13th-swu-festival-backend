package com.example.likelion_13th_swu_festival_backend.dto.answerDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnswerReturnDto {
    // is_win
    private boolean is_win;

    // answer
    private char choice;

    // a_rate
    private float a_rate;

    // b_rate
    private float b_rate;
}
