package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.Npc;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserNpcProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNpcProgressRepository extends JpaRepository<UserNpcProgress, Long> {

    Optional<UserNpcProgress> findByUserAndNpc(User user, Npc npc);
    List<UserNpcProgress> findByUser(User user);
    List<UserNpcProgress> findByUserAndUnlockedTrue(User user);

}
