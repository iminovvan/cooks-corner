package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.LoginRequestDto;
import com.example.cooks_corner.dto.RegisterRequestDto;
import com.example.cooks_corner.repository.TokenBlacklistRepository;
import com.example.cooks_corner.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(
        name = "Authentication and Authorization",
        description = "Endpoints for user registration, login, and logout"
)
public class AuthenticationController {
    private final AuthenticationService authService;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Operation(
            summary = "Register a new user",
            description = "Register a new user with email, name, and password.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Register request DTO containing email, name, and password.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registration successful", content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "400", description = "Email already exists or other validation error", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Role not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto){
        String jwtToken = authService.register(registerRequestDto);
        return ResponseEntity.ok(Map.of("token", jwtToken));
    }

    @Operation(
            summary = "Log in a user",
            description = "Log in a user with email and password.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login request DTO containing email and password.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid email or password", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        String jwtToken = authService.login(loginRequestDto);
        return ResponseEntity.ok(Map.of("token", jwtToken));
    }

    @Operation(
            summary = "Log out a user",
            description = "Log out a user by invalidating the JWT token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged out successfully", content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "400", description = "No token provided", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            },
            parameters = {
            @Parameter(
                    name = "Authorization",
                    description = "JWT token prefixed with Bearer",
                    required = true,
                    in = ParameterIn.HEADER,
                    schema = @Schema(type = "string")
            )
    }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            String jwtToken = tokenHeader.substring(7);
            tokenBlacklistRepository.addToken(jwtToken);
            return ResponseEntity.ok(Map.of("message", "User logged out successfully"));
        } else {
            return ResponseEntity.badRequest().body("No token provided");
        }
    }
}
