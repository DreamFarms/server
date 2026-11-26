package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.KneadingAttempt;
import com.server.esgcafe.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KneadingAttemptRepository extends JpaRepository<KneadingAttempt, Long> {

    Optional<KneadingAttempt> findByAttemptNoAndUser(Long attemptNo, User user);
}
