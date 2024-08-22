package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserBread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBreadRepository extends JpaRepository<UserBread, Long> {

    Optional<UserBread> findByUserAndFood(User user, Food food);
    List<UserBread> findByUser(User user);

}
