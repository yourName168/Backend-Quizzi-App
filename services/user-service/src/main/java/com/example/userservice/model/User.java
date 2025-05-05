package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    private LocalDateTime createdAt;

    @Column(unique = true)
    private String phoneNumber;

    private String fullName;

    private String lastName;

    private String country;

    private String dateOfBirth;

    private Integer age;

    private String avatar;

    private String resetPasswordCode;

    private Integer totalQuizs;

    private Integer totalCollections;

    private Integer totalPlays;

    private Integer totalFollowers;

    private Integer totalFollowees;

    private Integer totalPlayers;

    @OneToOne(cascade = CascadeType.ALL)
    private UserMusicEffect userMusicEffect;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private Set<UserFollow> following = new HashSet<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
    private Set<UserFollow> followers = new HashSet<>();
}