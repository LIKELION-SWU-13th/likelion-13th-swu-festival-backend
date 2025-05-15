package com.example.likelion_13th_swu_festival_backend.dto.boothDTO;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoothInfoResponse {
    private List<String> department_list;
    private String major;

    public BoothInfoResponse(List<String> boothNames, String major) {
        this.department_list = boothNames != null ? department_list : new ArrayList<>();
        this.major = major;
    }
}
