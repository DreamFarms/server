package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickName(String nickname);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.nickName = :nickname")
    Optional<User> findByNickNameForUpdate(@Param("nickname") String nickname);
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findByUserNo(Long userNo);
}
