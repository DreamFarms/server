package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.Npc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NpcRepository extends JpaRepository<Npc, Long> {

    Optional<Npc> findByName(String name);
    List<Npc> findByNameIn(List<String> name);

}
