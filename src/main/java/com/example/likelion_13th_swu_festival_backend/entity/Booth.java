package com.example.likelion_13th_swu_festival_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
public class Booth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;  // 부스 이름 (예: "소프트웨어학과", "디지털미디어학과")

    //@Column(name = "description")
    //private String description;  // 부스 운영 내용 / 판매 정보

    @Column(name = "is_active")
    private Boolean isActive;  // 운영 여부 (운영중 / 종료)

}
