package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.model.UserFollow;
import com.example.userservice.service.UserFollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-follows")
@RequiredArgsConstructor
@Tag(name = "User Follow Management", description = "APIs for managing user following relationships")
public class UserFollowController {
    private final UserFollowService userFollowService;

    @PostMapping("/{followerId}/follow/{followeeId}")
    @Operation(summary = "Follow a user", description = "Creates a new following relationship between users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Following relationship created successfully",
                content = @Content(schema = @Schema(implementation = UserFollow.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Already following")
    })
    public ResponseEntity<UserFollow> followUser(@PathVariable @Parameter(description = "ID of the user who wants to follow") Long followerId, 
                                                 @PathVariable @Parameter(description = "ID of the user to be followed") Long followeeId) {
        return new ResponseEntity<>(userFollowService.followUser(followerId, followeeId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{followerId}/unfollow/{followeeId}")
    @Operation(summary = "Unfollow a user", description = "Removes a following relationship between users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Unfollowed successfully"),
        @ApiResponse(responseCode = "404", description = "Following relationship not found")
    })
    public ResponseEntity<Void> unfollowUser(@PathVariable @Parameter(description = "ID of the user who wants to unfollow") Long followerId, 
                                             @PathVariable @Parameter(description = "ID of the user to be unfollowed") Long followeeId) {
        userFollowService.unfollowUser(followerId, followeeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    @Operation(summary = "Get followers", description = "Retrieves all followers of a user")
    @ApiResponse(responseCode = "200", description = "Followers retrieved successfully",
            content = @Content(schema = @Schema(implementation = User.class)))
    public ResponseEntity<List<User>> getFollowers(@PathVariable @Parameter(description = "User ID") Long userId) {
        return ResponseEntity.ok(userFollowService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    @Operation(summary = "Get following", description = "Retrieves all users that a user is following")
    @ApiResponse(responseCode = "200", description = "Following list retrieved successfully",
            content = @Content(schema = @Schema(implementation = User.class)))
    public ResponseEntity<List<User>> getFollowing(@PathVariable @Parameter(description = "User ID") Long userId) {
        return ResponseEntity.ok(userFollowService.getFollowing(userId));
    }

    @GetMapping("/{followerId}/is-following/{followeeId}")
    @Operation(summary = "Check if following", description = "Checks if one user is following another user")
    @ApiResponse(responseCode = "200", description = "Following status retrieved successfully",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<Boolean> isFollowing(@PathVariable @Parameter(description = "ID of the follower") Long followerId, 
                                               @PathVariable @Parameter(description = "ID of the followee") Long followeeId) {
        return ResponseEntity.ok(userFollowService.isFollowing(followerId, followeeId));
    }
}