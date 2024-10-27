package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.FoodRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRecipeRepository extends JpaRepository<FoodRecipe, Long> {
}
