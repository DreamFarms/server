package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.Quest;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserQuestProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserQuestProgressRepository extends JpaRepository<UserQuestProgress, Long> {

    Optional<UserQuestProgress> findByUserAndQuest(User user, Quest quest);



}
