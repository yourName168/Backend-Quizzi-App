package com.example.userservice.util;

import com.github.javafaker.Faker;
import com.example.userservice.model.User;
import com.example.userservice.model.UserFollow;
import com.example.userservice.model.UserMusicEffect;
import com.example.userservice.repository.UserFollowRepository;
import com.example.userservice.repository.UserMusicEffectRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataGenerator implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserMusicEffectRepository userMusicEffectRepository;
    private final UserFollowRepository userFollowRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();

    @Override
    public void run(String... args) {
        // Generate users if none exist
        if (userRepository.count() == 0) {
            generateUsers(50);
            generateFollowRelationships();
        }
    }

    private void generateUsers(int count) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            // Create user music effect
            UserMusicEffect musicEffect = UserMusicEffect.builder()
                    .music(faker.bool().bool())
                    .soundEffects(faker.bool().bool())
                    .animationEffects(faker.bool().bool())
                    .visualEffects(faker.bool().bool())
                    .build();

            // userMusicEffectRepository.save(musicEffect);

            // Create user
            Date birthDate = faker.date().birthday(18, 70);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(birthDate);

            User user = User.builder()
                    .username(faker.name().username())
                    .password(passwordEncoder.encode("password"))
                    .email(faker.internet().emailAddress())
                    .createdAt(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 365)))
                    .phoneNumber(faker.phoneNumber().cellPhone())
                    .fullName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .country(faker.address().country())
                    .dateOfBirth(calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                            (calendar.get(Calendar.MONTH) + 1) + "/" +
                            calendar.get(Calendar.YEAR))
                    .age(LocalDateTime.now().getYear() - calendar.get(Calendar.YEAR))
                    .avatar(faker.internet().avatar())
                    .resetPasswordCode(null)
                    .totalQuizs(faker.number().numberBetween(0, 20))
                    .totalCollections(faker.number().numberBetween(0, 10))
                    .totalPlays(faker.number().numberBetween(0, 100))
                    .totalFollowers(0)
                    .totalFollowees(0)
                    .totalPlayers(faker.number().numberBetween(0, 50))
                    .userMusicEffect(musicEffect)
                    .build();

            users.add(user);
        }

        userRepository.saveAll(users);
        System.out.println("Generated " + count + " users");
    }

    private void generateFollowRelationships() {
        List<User> users = userRepository.findAll();
        Random random = new Random();

        // For each user, follow 0-10 random other users
        for (User follower : users) {
            int followCount = random.nextInt(10);

            for (int i = 0; i < followCount; i++) {
                User followee = users.get(random.nextInt(users.size()));

                // Don't follow self or users already followed
                if (followee.getId().equals(follower.getId()) ||
                        userFollowRepository.existsByFollowerAndFollowee(follower, followee)) {
                    continue;
                }

                UserFollow follow = UserFollow.builder()
                        .follower(follower)
                        .followee(followee)
                        .build();

                // Update follower and followee counts
                follower.setTotalFollowees(follower.getTotalFollowees() + 1);
                followee.setTotalFollowers(followee.getTotalFollowers() + 1);

                userFollowRepository.save(follow);
            }

            userRepository.save(follower);
        }

        System.out.println("Generated follow relationships");
    }
}