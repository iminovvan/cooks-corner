package com.example.cooks_corner.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.cooks_corner.entity.User;
import com.example.cooks_corner.exception.TokenValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtService {
    private static final String SECRET_KEY = System.getenv("SECRET_KEY");
    private static final String TOKEN_EMAIL = "email";

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().getAuthority());
        claims.put(TOKEN_EMAIL, user.getEmail());

        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");

        long expirationMillis = 12 * 60 * 60 * 1000L;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token, User user) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
            Algorithm algorithm = Algorithm.HMAC256(keyBytes);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            String email = decodedJWT.getClaim(TOKEN_EMAIL).asString();
            boolean isExpired = isTokenExpired(decodedJWT);
            return (email.equals(user.getEmail()) && !isExpired);
        } catch (JWTVerificationException ex){
            throw new TokenValidationException("Token validation failed: {}" + ex.getMessage());
        } catch (Exception ex){
            throw new TokenValidationException("Token validation failed: {}" + ex.getMessage());
        }
    }

    public Boolean isTokenExpired(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().before(new Date());
    }

    private Claims getAllClaimsFromToken(String token){
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public String extractEmail(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
