package com.bookmyshow.movie.controller;

import com.bookmyshow.movie.services.userSvc.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;
import java.util.Map;

@RestController
public class TokenController {


    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/protocol/openid-connect/token/introspect")
    public ResponseEntity<?> introspect(@RequestHeader("Authorization") String token){

        if(token==null ||token.isBlank()){
            return ResponseEntity.badRequest().body("Token is empty");
        }

        if(!token.startsWith("Bearer")){
            return ResponseEntity.badRequest().body("Token should start with a bearer");
        }

        token = token.substring(7);

        return ResponseEntity.ok(tokenService.introspect(token));
    }


    @RequestMapping("/refreshToken")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> token){

        if(token==null ||token.get("token").isBlank()){
            return ResponseEntity.badRequest().body("Token is empty");
        }

        try {
            return ResponseEntity.ok(tokenService.refresh(token.get("token")));
        } catch (CredentialException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/isTokenValid")
    public ResponseEntity<Boolean> isTokenValid(@RequestHeader("Authorization") String token){

        if(token==null ||token.isBlank()){
            return ResponseEntity.badRequest().body(false);
        }

        if(!token.startsWith("Bearer")){
            return ResponseEntity.badRequest().body(false);
        }

        token = token.substring(7);
        if(tokenService.isTokenValid(token))
            return ResponseEntity.ok().body(true);
        else
            return ResponseEntity.ok().body(false);
    }
}
