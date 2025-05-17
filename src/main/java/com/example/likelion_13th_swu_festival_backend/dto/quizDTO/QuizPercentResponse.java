package com.example.likelion_13th_swu_festival_backend.dto.quizDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"choice", "a_rate", "b_rate"})
public class QuizPercentResponse {
    private String choice;
    private double a_rate;
    private double b_rate;
}