package com.example.likelion_13th_swu_festival_backend.dto.boothDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoothInfoResponse {
    private List<String> department_list;
    private String major;
}
