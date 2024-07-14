package com.example.cooks_corner.repository;

import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class TokenBlacklistRepository {
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void addToken(String token){
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token){
        return blacklistedTokens.contains(token);
    }
}
