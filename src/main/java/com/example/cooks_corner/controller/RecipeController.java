package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.RecipeRequestDto;
import com.example.cooks_corner.dto.RecipeResponseDto;
import com.example.cooks_corner.dto.RecipeShortResponseDto;
import com.example.cooks_corner.dto.UserEditDto;
import com.example.cooks_corner.entity.Recipe;
import com.example.cooks_corner.service.RecipeService;
import com.example.cooks_corner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
@Tag(
        name = "Recipe Management",
        description = "Endpoints for managing recipes, including recipe creation, details, likes, saves and searching recipes."
)
public class RecipeController {
    private final RecipeService recipeService;

    @Operation(
            summary = "Create a new recipe",
            description = "Create a new recipe with the provided details and image.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Recipe request DTO and image file.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = RecipeRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recipe created successfully", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
                    @ApiResponse(responseCode = "409", description = "User not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRecipe(@RequestPart("dto") RecipeRequestDto recipeRequestDto,
                                          @RequestPart("image") MultipartFile image, Principal principal){
        recipeService.createRecipe(recipeRequestDto, image, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe created successfully"));
    }

    @Operation(
            summary = "Like a recipe",
            description = "Allows a user to like a recipe by providing the recipe ID.",
            parameters = {
                    @Parameter(
                            name = "recipeId",
                            description = "ID of the recipe to like",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recipe liked successfully", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Use has already liked this recipe", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Recipe not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping("/likes/add/{recipeId}")
    public ResponseEntity<?> likeRecipe(@PathVariable("recipeId") Long id, Principal principal){
        recipeService.likeRecipe(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe liked successfully"));
    }

    @Operation(
            summary = "Unlike a recipe",
            description = "Allows a user to unlike a recipe by providing the recipe ID.",
            parameters = {
                    @Parameter(
                            name = "recipeId",
                            description = "ID of the recipe to unlike",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recipe unliked successfully", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Use has not liked this recipe", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Recipe not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping("/likes/remove/{recipeId}")
    public ResponseEntity<?> unlikeRecipe(@PathVariable("recipeId") Long id, Principal principal){
        recipeService.unlikeRecipe(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe unliked successfully"));
    }

    @Operation(
            summary = "Save a recipe",
            description = "Allows a user to save a recipe by providing the recipe ID.",
            parameters = {
                    @Parameter(
                            name = "recipeId",
                            description = "ID of the recipe to save",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recipe saved successfully", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Use has already saved this recipe", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Recipe not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping("/saves/add/{recipeId}")
    public ResponseEntity<?> saveRecipe(@PathVariable("recipeId") Long id, Principal principal){
        recipeService.saveRecipe(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe saved successfully"));
    }

    @Operation(
            summary = "Remove saved recipe",
            description = "Allows a user to remove saved recipe by providing the recipe ID.",
            parameters = {
                    @Parameter(
                            name = "recipeId",
                            description = "ID of the recipe to remove from saved",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recipe saved successfully", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Use has not saved this recipe", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Recipe not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping("/saves/remove/{recipeId}")
    public ResponseEntity<?> removeSavedRecipe(@PathVariable("recipeId") Long id, Principal principal){
        recipeService.removeSavedRecipe(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe unsaved successfully"));
    }

    @Operation(
            summary = "Find a recipe by ID",
            description = "Retrieve the details of a recipe by providing its ID.",
            parameters = {
                    @Parameter(
                            name = "recipeId",
                            description = "ID of the recipe to retrieve",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recipe retrieved successfully",
                            content = @Content(
                                    schema = @Schema(implementation = RecipeResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid recipe ID", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Recipe not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping("/{recipeId}")
    public ResponseEntity<?> findRecipeById(@PathVariable("recipeId") Long id){
        RecipeResponseDto recipeResponseDto = recipeService.findRecipeById(id);
        return ResponseEntity.ok(recipeResponseDto);
    }


    @Operation(
            summary = "Search recipes by title",
            description = "Retrieve a list of recipes that match the provided title.",
            parameters = {
                    @Parameter(
                            name = "title",
                            description = "Title of the recipe to search for",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recipes retrieved successfully",
                            content = @Content(
                                    schema = @Schema(implementation = RecipeShortResponseDto[].class),
                                    examples = @ExampleObject(
                                            value = "[{\"id\": 1, \"title\": \"Pancakes\", \"userName\": \"john_doe\", \"imageUrl\": \"https://res.cloudinary.com/demo/image/upload/v1620000000/sample.jpg\", \"likesCount\": 150, \"savesCount\": 75}]"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid title parameter", content = @Content),
                    @ApiResponse(responseCode = "404", description = "No recipes found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping("/search")
    public ResponseEntity<?> findRecipeByTitle(@RequestParam(name = "title") String title){
        List<RecipeShortResponseDto> recipes = recipeService.findRecipeByTitle(title);
        return ResponseEntity.ok(recipes);
    }

    @Operation(
            summary = "Retrieve recipes by category",
            description = "Retrieve a list of recipes that match the provided category.",
            parameters = {
                    @Parameter(
                            name = "category",
                            description = "Category of the recipe to retrieve",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recipes retrieved successfully",
                            content = @Content(
                                    schema = @Schema(implementation = RecipeShortResponseDto[].class),
                                    examples = @ExampleObject(
                                            value = "[{\"id\": 1, \"title\": \"Pancakes\", \"userName\": \"John Doe\", \"imageUrl\": \"https://res.cloudinary.com/demo/image/upload/v1620000000/sample.jpg\", \"likesCount\": 150, \"savesCount\": 75}]"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "No recipes found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<?> findRecipesByCategory(@RequestParam(name = "category") String category){
        List<RecipeShortResponseDto> recipes = recipeService.findRecipesByCategory(category);
        return ResponseEntity.ok(recipes);
    }
}
