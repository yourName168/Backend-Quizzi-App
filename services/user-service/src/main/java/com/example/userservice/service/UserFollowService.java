package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.model.UserFollow;
import com.example.userservice.repository.UserFollowRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFollowService {
    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;

    public UserFollow followUser(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new RuntimeException("User cannot follow themselves");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new RuntimeException("Followee not found"));

        if (userFollowRepository.existsByFollowerAndFollowee(follower, followee)) {
            throw new RuntimeException("Already following this user");
        }

        UserFollow userFollow = UserFollow.builder()
                .follower(follower)
                .followee(followee)
                .build();

        // Update user statistics
        follower.setTotalFollowees(follower.getTotalFollowees() + 1);
        followee.setTotalFollowers(followee.getTotalFollowers() + 1);

        userRepository.save(follower);
        userRepository.save(followee);

        return userFollowRepository.save(userFollow);
    }

    public void unfollowUser(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new RuntimeException("Followee not found"));

        UserFollow userFollow = userFollowRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(() -> new RuntimeException("Follow relationship not found"));

        // Update user statistics
        follower.setTotalFollowees(follower.getTotalFollowees() - 1);
        followee.setTotalFollowers(followee.getTotalFollowers() - 1);

        userRepository.save(follower);
        userRepository.save(followee);

        userFollowRepository.delete(userFollow);
    }

    public List<User> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userFollowRepository.findByFollowee(user).stream()
                .map(UserFollow::getFollower)
                .collect(Collectors.toList());
    }

    public List<User> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userFollowRepository.findByFollower(user).stream()
                .map(UserFollow::getFollowee)
                .collect(Collectors.toList());
    }

    public boolean isFollowing(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new RuntimeException("Followee not found"));

        return userFollowRepository.existsByFollowerAndFollowee(follower, followee);
    }
}

