package com.example.likelion_13th_swu_festival_backend.controller;

import com.example.likelion_13th_swu_festival_backend.dto.answerDTO.AnswerChoiceDto;
import com.example.likelion_13th_swu_festival_backend.dto.answerDTO.AnswerReturnDto;
import com.example.likelion_13th_swu_festival_backend.security.CustomUserDetails;
import com.example.likelion_13th_swu_festival_backend.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answers")
public class AnswerController {
    final AnswerService answerService;

    @PostMapping("/sendAnswer/{quiz_id}")
    public ResponseEntity<?> addAnswer (@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody AnswerChoiceDto answerChoiceDto, @PathVariable Long quiz_id){
        try{
            char choice = answerChoiceDto.getChoiceStr().charAt(0);
            boolean is_win = answerService.saveAnswer(customUserDetails.getUserId() ,choice, quiz_id);

            AnswerReturnDto answerReturnDto = answerService.calculateVoterTurnout(quiz_id, is_win, choice);

            return ResponseEntity.ok(answerReturnDto);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( e.getMessage());
        }
    }
}
