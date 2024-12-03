package com.example.LetsPlay.webToken;

import java.sql.Date;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
@RestController
public class JwtService {

    private static final String SECRET = "C79DD357C0FC53822BB123696439743D8A859F4A307A9B02A60A78CD841B6D350780649718E0561197AE668B7BB02E135E7F6A082981716EE1287BBC3B663992";
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(3);

    public String generateToken(String username, String role) {
          Map<String, String> claims = new HashMap<>();
          claims.put("iss", "https://localhost:3030");
        return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
            .signWith(generateKey())
            .compact(); // Convert to JSON
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

        public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                 .verifyWith(generateKey())
                 .build()
                 .parseSignedClaims(jwt)
                 .getPayload();
    }
}
