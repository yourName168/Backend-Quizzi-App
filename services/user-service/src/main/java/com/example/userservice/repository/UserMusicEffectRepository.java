package com.example.userservice.repository;

import com.example.userservice.model.UserMusicEffect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMusicEffectRepository extends JpaRepository<UserMusicEffect, Long> {
}