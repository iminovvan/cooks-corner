package com.example.cooks_corner;

import com.example.cooks_corner.controller.RecipeController;
import com.example.cooks_corner.dto.RecipeRequestDto;
import com.example.cooks_corner.dto.RecipeResponseDto;
import com.example.cooks_corner.dto.RecipeShortResponseDto;
import com.example.cooks_corner.exception.GlobalExceptionHandler;
import com.example.cooks_corner.exception.NotFoundException;
import com.example.cooks_corner.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateRecipeSuccess() throws Exception {
        RecipeRequestDto requestDto = new RecipeRequestDto();
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".getBytes());
        Principal principal = () -> "test@example.com";

        doNothing().when(recipeService).createRecipe(any(RecipeRequestDto.class), any(MultipartFile.class), anyString());

        var result = recipeController.createRecipe(requestDto, image, principal);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(Map.of("message", "Recipe created successfully"), result.getBody());
    }

    @Test
    void testCreateRecipeUserNotFound() throws Exception {
        RecipeRequestDto requestDto = new RecipeRequestDto();
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".getBytes());
        Principal principal = () -> "test@example.com";

        doThrow(new NotFoundException("User not found")).when(recipeService).createRecipe(any(RecipeRequestDto.class), any(MultipartFile.class), anyString());

        var exception = assertThrows(NotFoundException.class, () -> recipeController.createRecipe(requestDto, image, principal));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLikeRecipeSuccess() {
        Principal principal = () -> "test@example.com";

        doNothing().when(recipeService).likeRecipe(anyLong(), anyString());

        var result = recipeController.likeRecipe(1L, principal);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(Map.of("message", "Recipe liked successfully"), result.getBody());
    }

    @Test
    void testLikeRecipeNotFound() {
        Principal principal = () -> "test@example.com";

        doThrow(new NotFoundException("Recipe not found")).when(recipeService).likeRecipe(anyLong(), anyString());

        var exception = assertThrows(NotFoundException.class, () -> recipeController.likeRecipe(1L, principal));

        assertEquals("Recipe not found", exception.getMessage());
    }

    @Test
    void testUnlikeRecipeSuccess() {
        Principal principal = () -> "test@example.com";

        doNothing().when(recipeService).unlikeRecipe(anyLong(), anyString());

        var result = recipeController.unlikeRecipe(1L, principal);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(Map.of("message", "Recipe unliked successfully"), result.getBody());
    }

    @Test
    void testUnlikeRecipeNotFound() {
        Principal principal = () -> "test@example.com";

        doThrow(new NotFoundException("Recipe not found")).when(recipeService).unlikeRecipe(anyLong(), anyString());

        var exception = assertThrows(NotFoundException.class, () -> recipeController.unlikeRecipe(1L, principal));

        assertEquals("Recipe not found", exception.getMessage());
    }

    @Test
    void testSaveRecipeSuccess() {
        Principal principal = () -> "test@example.com";

        doNothing().when(recipeService).saveRecipe(anyLong(), anyString());

        var result = recipeController.saveRecipe(1L, principal);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(Map.of("message", "Recipe saved successfully"), result.getBody());
    }

    @Test
    void testSaveRecipeNotFound() {
        Principal principal = () -> "test@example.com";

        doThrow(new NotFoundException("Recipe not found")).when(recipeService).saveRecipe(anyLong(), anyString());

        var exception = assertThrows(NotFoundException.class, () -> recipeController.saveRecipe(1L, principal));

        assertEquals("Recipe not found", exception.getMessage());
    }

    @Test
    void testRemoveSavedRecipeSuccess() {
        Principal principal = () -> "test@example.com";

        doNothing().when(recipeService).removeSavedRecipe(anyLong(), anyString());

        var result = recipeController.removeSavedRecipe(1L, principal);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(Map.of("message", "Recipe unsaved successfully"), result.getBody());
    }

    @Test
    void testRemoveSavedRecipeNotFound() {
        Principal principal = () -> "test@example.com";

        doThrow(new NotFoundException("Recipe not found")).when(recipeService).removeSavedRecipe(anyLong(), anyString());

        var exception = assertThrows(NotFoundException.class, () -> recipeController.removeSavedRecipe(1L, principal));

        assertEquals("Recipe not found", exception.getMessage());
    }

    @Test
    void testFindRecipeByIdSuccess() {
        RecipeResponseDto responseDto = new RecipeResponseDto();
        responseDto.setId(1L);

        when(recipeService.findRecipeById(anyLong())).thenReturn(responseDto);

        var result = recipeController.findRecipeById(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void testFindRecipeByIdNotFound() {
        doThrow(new NotFoundException("Recipe not found")).when(recipeService).findRecipeById(anyLong());

        var exception = assertThrows(NotFoundException.class, () -> recipeController.findRecipeById(1L));

        assertEquals("Recipe not found", exception.getMessage());
    }

    @Test
    void testFindRecipeByTitleSuccess() {
        RecipeShortResponseDto responseDto = new RecipeShortResponseDto();
        responseDto.setId(1L);

        when(recipeService.findRecipeByTitle(anyString())).thenReturn(List.of(responseDto));

        var result = recipeController.findRecipeByTitle("Pancakes");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(List.of(responseDto), result.getBody());
    }

    @Test
    void testFindRecipeByTitleNotFound() {
        when(recipeService.findRecipeByTitle(anyString())).thenReturn(Collections.emptyList());

        var result = recipeController.findRecipeByTitle("NonExistingTitle");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(Collections.emptyList(), result.getBody());
    }

    @Test
    void testFindRecipesByCategorySuccess() {
        RecipeShortResponseDto responseDto = new RecipeShortResponseDto();
        responseDto.setId(1L);

        when(recipeService.findRecipesByCategory(anyString())).thenReturn(List.of(responseDto));

        var result = recipeController.findRecipesByCategory("BREAKFAST");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(List.of(responseDto), result.getBody());
    }

    @Test
    void testFindRecipesByCategoryNotFound() {
        when(recipeService.findRecipesByCategory(anyString())).thenReturn(Collections.emptyList());

        var result = recipeController.findRecipesByCategory("NonExistingCategory");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(Collections.emptyList(), result.getBody());
    }
}
