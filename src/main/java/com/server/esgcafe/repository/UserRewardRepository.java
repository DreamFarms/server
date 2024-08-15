package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRewardRepository extends JpaRepository<UserReward, Long> {

    Optional<UserReward> findByRewardName(String rewardName);
    List<UserReward> findByUser(User user);
}
