package com.example.cooks_corner.config;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        String cloudName = System.getenv("CLOUD_NAME");
        String apiKey = System.getenv("API_KEY");
        String apiSecret = System.getenv("API_SECRET");

        if (cloudName == null || apiKey == null || apiSecret == null) {
            throw new IllegalArgumentException("Cloudinary environment variables not set");
        }

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        return new Cloudinary(config);
    }
}
