package com.example.cooks_corner;

import com.example.cooks_corner.controller.AuthenticationController;
import com.example.cooks_corner.dto.LoginRequestDto;
import com.example.cooks_corner.dto.RegisterRequestDto;
import com.example.cooks_corner.exception.GlobalExceptionHandler;
import com.example.cooks_corner.exception.InvalidCredentialsException;
import com.example.cooks_corner.exception.NotFoundException;
import com.example.cooks_corner.exception.UserExistsException;
import com.example.cooks_corner.repository.TokenBlacklistRepository;
import com.example.cooks_corner.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authService;

    @Mock
    private TokenBlacklistRepository tokenBlacklistRepository;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setName("Test User");
        requestDto.setPassword("password");

        when(authService.register(any(RegisterRequestDto.class))).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"name\": \"Test User\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void testRegisterUserExists() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setName("Test User");
        requestDto.setPassword("password");

        when(authService.register(any(RegisterRequestDto.class))).thenThrow(new UserExistsException("Email already exists"));

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"name\": \"Test User\", \"password\": \"password\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email already exists"));
    }

    @Test
    void testRegisterRoleNotFound() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setName("Test User");
        requestDto.setPassword("password");

        when(authService.register(any(RegisterRequestDto.class))).thenThrow(new NotFoundException("Role not found."));

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"name\": \"Test User\", \"password\": \"password\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Role not found."));
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");

        when(authService.login(any(LoginRequestDto.class))).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");

        when(authService.login(any(LoginRequestDto.class))).thenThrow(new InvalidCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid email or password"));
    }

    @Test
    void testLogoutSuccess() throws Exception {
        doNothing().when(tokenBlacklistRepository).addToken("mocked-jwt-token");

        mockMvc.perform(post("/api/logout")
                        .header("Authorization", "Bearer mocked-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User logged out successfully"));

        verify(tokenBlacklistRepository, times(1)).addToken("mocked-jwt-token");
    }

    @Test
    void testLogoutNoToken() throws Exception {
        mockMvc.perform(post("/api/logout"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No token provided"));
    }
}
