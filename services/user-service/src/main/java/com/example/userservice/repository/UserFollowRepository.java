package com.example.userservice.repository;

import com.example.userservice.model.User;
import com.example.userservice.model.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    List<UserFollow> findByFollower(User follower);
    List<UserFollow> findByFollowee(User followee);
    Optional<UserFollow> findByFollowerAndFollowee(User follower, User followee);
    boolean existsByFollowerAndFollowee(User follower, User followee);
    int countByFollower(User follower);
    int countByFollowee(User followee);
}
