package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.Ingredient;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInventoryRepository extends JpaRepository<UserInventory, Long> {

//    Optional<UserInventory> findUserAndFood(User user, Food food);

//    Optional<UserInventory> findByUserAndIngredient(User user, Ingredient ingredient);

    List<UserInventory> findByUser(User user);
}
