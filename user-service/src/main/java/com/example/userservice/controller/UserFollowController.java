package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.model.UserFollow;
import com.example.userservice.service.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-follows")
@RequiredArgsConstructor
public class UserFollowController {
    private final UserFollowService userFollowService;

    @PostMapping("/{followerId}/follow/{followeeId}")
    public ResponseEntity<UserFollow> followUser(@PathVariable Long followerId, @PathVariable Long followeeId) {
        return new ResponseEntity<>(userFollowService.followUser(followerId, followeeId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{followerId}/unfollow/{followeeId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long followerId, @PathVariable Long followeeId) {
        userFollowService.unfollowUser(followerId, followeeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(userFollowService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(userFollowService.getFollowing(userId));
    }

    @GetMapping("/{followerId}/is-following/{followeeId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long followerId, @PathVariable Long followeeId) {
        return ResponseEntity.ok(userFollowService.isFollowing(followerId, followeeId));
    }
}

