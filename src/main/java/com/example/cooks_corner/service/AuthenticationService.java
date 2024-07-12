package com.example.cooks_corner.service;

import com.example.cooks_corner.dto.LoginRequestDto;
import com.example.cooks_corner.dto.RegisterRequestDto;
import com.example.cooks_corner.entity.Role;
import com.example.cooks_corner.entity.User;
import com.example.cooks_corner.exception.InvalidCredentialsException;
import com.example.cooks_corner.exception.NotFoundException;
import com.example.cooks_corner.exception.UserExistsException;
import com.example.cooks_corner.exception.ValidationException;
import com.example.cooks_corner.repository.RoleRepository;
import com.example.cooks_corner.repository.UserRepository;
import com.example.cooks_corner.util.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequestDto registerRequestDto){
        log.info("Registering user: {}", registerRequestDto.getEmail());
        if(userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()){
            throw new UserExistsException("Email already exists");
        }
        User user = modelMapper.map(registerRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByAuthority("USER").
                orElseThrow(() -> new NotFoundException("Role not found."));
        user.setRole(userRole);
        userRepository.save(user);
        log.info("Registered user: {}", registerRequestDto.getEmail());
        return jwtService.generateToken(user);
    }

    public String login(LoginRequestDto loginRequestDto){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
            );
            User user =(User) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(user);
            return jwtToken;
        } catch(AuthenticationException ex){
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    public String registerAdmin(RegisterRequestDto registerRequestDto){
        log.info("Registering admin: {}", registerRequestDto.getEmail());
        if(userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()){
            throw new UserExistsException("Email already exists");
        }
        User user = modelMapper.map(registerRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role adminRole = roleRepository.findByAuthority("ADMIN").
                orElseThrow(() -> new NotFoundException("Role not found."));
        user.setRole(adminRole);
        userRepository.save(user);
        return jwtService.generateToken(user);
    }
}

