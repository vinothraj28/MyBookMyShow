package com.bookmyshow.movie.jwtoken;

import com.bookmyshow.movie.model.userCode.MovieRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwUtil {

    @Value("${jwt.JwSecretKey}")
    private String JwSecretKey;

    @Value("${jwt.JwExpirationMs}")
    private int JwExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(JwSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, List<MovieRole> roles){

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(JwExpirationMs);

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();

    }

    public String generateToken(String username, String redirectURI){

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(JwExpirationMs);

        return Jwts.builder()
                .claims(Map.of("redirectURI", redirectURI))
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username){

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(JwExpirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }


    public boolean validate(String token){
        Date expiration;
        try{
            expiration = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Expiration time is "+expiration);
        return expiration.after(new Date());
    }

    public Claims extractClaims(String token){
        Claims claims;
        try{
            claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return claims;
    }


}
