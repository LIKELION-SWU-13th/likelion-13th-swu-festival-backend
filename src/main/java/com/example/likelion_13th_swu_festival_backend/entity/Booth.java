package com.example.likelion_13th_swu_festival_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter @Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Booth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;  // 부스 이름


    @Column(name = "is_active")
    private Boolean isActive;

    public Booth(String name, Boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }
}
