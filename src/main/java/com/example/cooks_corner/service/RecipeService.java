package com.example.cooks_corner.service;

import com.example.cooks_corner.dto.RecipeRequestDto;
import com.example.cooks_corner.dto.RecipeResponseDto;
import com.example.cooks_corner.dto.RecipeShortResponseDto;
import com.example.cooks_corner.entity.Image;
import com.example.cooks_corner.entity.Recipe;
import com.example.cooks_corner.entity.User;
import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.exception.ImageException;
import com.example.cooks_corner.exception.LikeException;
import com.example.cooks_corner.exception.NotFoundException;
import com.example.cooks_corner.repository.ImageRepository;
import com.example.cooks_corner.repository.IngredientRepository;
import com.example.cooks_corner.repository.RecipeRepository;
import com.example.cooks_corner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RecipeService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    public void createRecipe(RecipeRequestDto recipeRequestDto, MultipartFile image, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Recipe recipe = modelMapper.map(recipeRequestDto, Recipe.class);
        recipe.setUser(user);
        if(image == null){
            throw new ImageException("Image cannot be null");
        }
        try {
            Image recipeImage = new Image();
            recipeImage.setUrl(cloudinaryService.uploadFile(image, "recipeImages"));
            imageRepository.save(recipeImage);
            recipe.setImage(recipeImage);
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
        user.getCreatedRecipes().add(recipe);
        recipe.getIngredients().stream().forEach(ingredient -> ingredient.setRecipe(recipe));
        recipeRepository.save(recipe);
        ingredientRepository.saveAll(recipe.getIngredients());
    }

    public void likeRecipe(Long id, String email){
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getLikedRecipes().contains(recipe)) {
            throw new LikeException("User has already liked this recipe");
        }
        user.getLikedRecipes().add(recipe);
        recipe.getUsersWhoLiked().add(user);
        userRepository.save(user);
        recipeRepository.save(recipe);
    }

    public void unlikeRecipe(Long id, String email){
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!user.getLikedRecipes().contains(recipe)) {
            throw new LikeException("User has not liked this recipe");
        }
        user.getLikedRecipes().remove(recipe);
        recipe.getUsersWhoLiked().remove(user);

        userRepository.save(user);
        recipeRepository.save(recipe);
    }

    public void saveRecipe(Long id, String email){
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getSavedRecipes().contains(recipe)) {
            throw new LikeException("User has already saved this recipe");
        }
        user.getSavedRecipes().add(recipe);
        recipe.getUsersWhoSaved().add(user);
        userRepository.save(user);
        recipeRepository.save(recipe);
    }

    public void removeSavedRecipe(Long id, String email){
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!user.getSavedRecipes().contains(recipe)) {
            throw new LikeException("User has not saved this recipe");
        }
        user.getSavedRecipes().remove(recipe);
        recipe.getUsersWhoSaved().remove(user);
        userRepository.save(user);
        recipeRepository.save(recipe);
    }

    public RecipeResponseDto findRecipeById(Long id){
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));
        RecipeResponseDto recipeResponseDto = modelMapper.map(recipe, RecipeResponseDto.class);
        recipeResponseDto.setLikesCount(recipe.getUsersWhoLiked().size());
        recipeResponseDto.setSavesCount(recipe.getUsersWhoSaved().size());
        return recipeResponseDto;
    }

    public List<RecipeShortResponseDto> findRecipeByTitle(String title){
        List<Recipe> recipes = recipeRepository.findAllByTitleContainingIgnoreCase(title);
        List<RecipeShortResponseDto> recipeDtos = new ArrayList<>();
        setDto(recipeDtos, recipes);
        return recipeDtos;
    }

    public void setDto(List<RecipeShortResponseDto> recipeDtos, List<Recipe> recipes){
        for(Recipe recipe : recipes){
            RecipeShortResponseDto dto = modelMapper.map(recipe, RecipeShortResponseDto.class);
            dto.setLikesCount(recipe.getUsersWhoLiked().size());
            dto.setSavesCount(recipe.getUsersWhoSaved().size());
            dto.setId(recipe.getId());
            recipeDtos.add(dto);
        }
    }

    public List<RecipeShortResponseDto> findRecipesByCategory(String category){
        List<RecipeShortResponseDto> recipeDtos = new ArrayList<>();
        if(category.toUpperCase().equals(Category.BREAKFAST.toString())){
            List<Recipe> recipes = recipeRepository.findAllByCategory(Category.BREAKFAST);
            setDto(recipeDtos, recipes);
        } else if(category.toUpperCase().equals(Category.LUNCH.toString())){
            List<Recipe> recipes = recipeRepository.findAllByCategory(Category.LUNCH);
            setDto(recipeDtos, recipes);
        } else if(category.toUpperCase().equals(Category.DINNER.toString())){
            List<Recipe> recipes = recipeRepository.findAllByCategory(Category.DINNER);
            setDto(recipeDtos, recipes);
        }
        return recipeDtos;
    }
}
