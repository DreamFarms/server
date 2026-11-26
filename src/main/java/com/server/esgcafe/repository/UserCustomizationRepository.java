package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserCustomization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCustomizationRepository extends JpaRepository<UserCustomization, Long> {

    List<UserCustomization> findByUser(User user);

}
