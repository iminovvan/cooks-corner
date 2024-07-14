package com.example.cooks_corner.repository;

import com.example.cooks_corner.entity.Recipe;
import com.example.cooks_corner.entity.User;
import com.example.cooks_corner.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByTitleContainingIgnoreCase(String title);
    List<Recipe> findAllByCategory(Category category);

}
