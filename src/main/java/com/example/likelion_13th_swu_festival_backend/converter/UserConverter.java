package com.example.likelion_13th_swu_festival_backend.converter;

import com.example.likelion_13th_swu_festival_backend.dto.userDTO.UserRequestDTO;
import com.example.likelion_13th_swu_festival_backend.entity.User;

public class UserConverter {
    //사용자 생성
    public static User createUser(UserRequestDTO.CreateUserRqDTO request){
        return User.builder()
                .studentNum(request.getStudent_num())
                .name(request.getName())
                .major(request.getMajor())
                .build();
    }

}