package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserUnlockedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserUnlockedRecipeRepository extends JpaRepository<UserUnlockedRecipe, Long> {

    // 특정 유저가 해금한 레시피 리스트를 가져오는 메서드
    List<UserUnlockedRecipe> findByUser(User user);

}