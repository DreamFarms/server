package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {

    Optional<Quest> findByQuestCode(Long questCode);

}
