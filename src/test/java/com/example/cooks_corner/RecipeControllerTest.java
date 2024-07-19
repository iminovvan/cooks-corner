package com.example.cooks_corner;

import com.example.cooks_corner.controller.RecipeController;
import com.example.cooks_corner.dto.RecipeRequestDto;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

}
