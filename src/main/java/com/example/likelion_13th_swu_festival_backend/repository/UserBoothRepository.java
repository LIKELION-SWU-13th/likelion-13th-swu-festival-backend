package com.example.likelion_13th_swu_festival_backend.repository;

import com.example.likelion_13th_swu_festival_backend.entity.Booth;
import com.example.likelion_13th_swu_festival_backend.entity.User;
import com.example.likelion_13th_swu_festival_backend.entity.UserBooth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBoothRepository extends JpaRepository<UserBooth, Long> {
    boolean existsByUserAndBooth(User user, Booth booth);
}
