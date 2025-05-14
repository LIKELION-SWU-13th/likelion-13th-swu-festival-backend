package com.example.likelion_13th_swu_festival_backend.dto.quizDTO;

import lombok.Data;

@Data
public class QuizAnswerResponseDto {
    private String choice;
    private boolean isWin;
    private double aRate;
    private double bRate;

    public void setIsWin(boolean isWin) {
        this.isWin = isWin;
    }

}
