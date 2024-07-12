package com.example.cooks_corner.util;

import com.example.cooks_corner.entity.Role;
import com.example.cooks_corner.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void seed() {
        if (!roleRepository.findByAuthority("USER").isPresent()) {
            Role userRole = new Role("USER");
            roleRepository.save(userRole);
        }

        if (!roleRepository.findByAuthority("ADMIN").isPresent()) {
            Role adminRole = new Role("ADMIN");
            roleRepository.save(adminRole);
        }
    }
}
