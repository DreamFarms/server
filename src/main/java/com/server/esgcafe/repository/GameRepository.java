package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.Game;
import com.server.esgcafe.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByUser(User user);

}
