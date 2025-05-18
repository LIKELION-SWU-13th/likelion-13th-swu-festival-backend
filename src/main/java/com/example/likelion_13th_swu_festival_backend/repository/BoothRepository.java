package com.example.likelion_13th_swu_festival_backend.repository;

import com.example.likelion_13th_swu_festival_backend.entity.Booth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoothRepository extends JpaRepository<Booth, Long> {

    List<Booth> findByIdBetween(Long startId, Long endId);

}
