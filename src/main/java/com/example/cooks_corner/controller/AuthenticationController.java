package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.LoginRequestDto;
import com.example.cooks_corner.dto.RegisterRequestDto;
import com.example.cooks_corner.repository.TokenBlacklistRepository;
import com.example.cooks_corner.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {
    private final AuthenticationService authService;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto){
        String jwtToken = authService.register(registerRequestDto);
        return ResponseEntity.ok(Map.of("token", jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        String jwtToken = authService.login(loginRequestDto);
        return ResponseEntity.ok(Map.of("token", jwtToken));
    }

    @GetMapping("/test/protected")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("You have access to a protected source!");
    }

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
