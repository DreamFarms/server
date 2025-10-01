package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.Npc;
import com.server.esgcafe.domain.entity.NpcPreferredFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NpcPreferredFoodRepository extends JpaRepository<NpcPreferredFood, Long> {

    List<NpcPreferredFood> findByNpcInOrderByNpcAscSortOrderAsc(List<Npc> npcs);
    List<NpcPreferredFood> findByNpcOrderByNpcAsc(Npc npc);

}
