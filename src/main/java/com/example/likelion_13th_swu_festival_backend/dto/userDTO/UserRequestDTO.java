package com.example.likelion_13th_swu_festival_backend.dto.userDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class UserRequestDTO {
    @Getter
    public static class CreateUserRqDTO{
        @NotNull
        @NotBlank
        String student_num;

        @NotNull
        @NotBlank
        String name;

        @NotNull
        @NotBlank
        String major;
    }
}