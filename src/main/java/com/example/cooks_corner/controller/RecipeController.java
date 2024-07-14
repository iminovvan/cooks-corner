package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.RecipeRequestDto;
import com.example.cooks_corner.dto.RecipeResponseDto;
import com.example.cooks_corner.dto.RecipeShortResponseDto;
import com.example.cooks_corner.dto.UserEditDto;
import com.example.cooks_corner.entity.Recipe;
import com.example.cooks_corner.service.RecipeService;
import com.example.cooks_corner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping("/create")
    public ResponseEntity<?> createRecipe(@RequestPart("dto") RecipeRequestDto recipeRequestDto,
                                          @RequestPart("image") MultipartFile image, Principal principal){
        recipeService.createRecipe(recipeRequestDto, image, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe created successfully"));
    }

    @GetMapping("/likes/add/{recipeId}")
    public ResponseEntity<?> likeRecipe(@PathVariable("recipeId") Long id, Principal principal){
        recipeService.likeRecipe(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe liked successfully"));
    }

    @GetMapping("/likes/remove/{recipeId}")
    public ResponseEntity<?> unlikeRecipe(@PathVariable("recipeId") Long id, Principal principal){
        recipeService.unlikeRecipe(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe unliked successfully"));
    }

    @GetMapping("/saves/add/{recipeId}")
    public ResponseEntity<?> saveRecipe(@PathVariable("recipeId") Long id, Principal principal){
        recipeService.saveRecipe(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe saved successfully"));
    }

    @GetMapping("/saves/remove/{recipeId}")
    public ResponseEntity<?> removeSavedRecipe(@PathVariable("recipeId") Long id, Principal principal){
        recipeService.removeSavedRecipe(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe unsaved successfully"));
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<?> findRecipeById(@PathVariable("recipeId") Long id){
        RecipeResponseDto recipeResponseDto = recipeService.findRecipeById(id);
        return ResponseEntity.ok(recipeResponseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<?> findRecipeByTitle(@RequestParam(name = "title") String title){
        List<RecipeShortResponseDto> recipes = recipeService.findRecipeByTitle(title);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping
    public ResponseEntity<?> findRecipesByCategory(@RequestParam(name = "category") String category){
        List<RecipeShortResponseDto> recipes = recipeService.findRecipesByCategory(category);
        return ResponseEntity.ok(recipes);
    }


}
