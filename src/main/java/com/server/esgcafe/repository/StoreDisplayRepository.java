package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.StoreDisplay;
import com.server.esgcafe.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreDisplayRepository extends JpaRepository<StoreDisplay, Long> {

    Optional<StoreDisplay> findByUserAndTableNoAndSlotNo(User user, Integer tableNo, Integer slotNo);

    List<StoreDisplay> findByUser(User user);

    List<StoreDisplay> findByUserAndTableNo(User user, Integer tableNo);
}
