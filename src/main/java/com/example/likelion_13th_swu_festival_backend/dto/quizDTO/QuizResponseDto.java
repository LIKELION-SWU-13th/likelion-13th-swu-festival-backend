package com.example.likelion_13th_swu_festival_backend.dto.quizDTO;

import com.example.likelion_13th_swu_festival_backend.constant.QuizStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class QuizResponseDto {
    private Long id;
    private String body;
    private String a_body;
    private String b_body;
}
