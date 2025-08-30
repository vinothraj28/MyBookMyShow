package com.bookmyshow.movie.controller;

import com.bookmyshow.movie.dto.UserRequestDTO;
import com.bookmyshow.movie.dto.UserResponseDTO;
import com.bookmyshow.movie.model.userCode.MovieRole;
import com.bookmyshow.movie.model.userCode.MovieUser;
import com.bookmyshow.movie.model.userCode.Token;
import com.bookmyshow.movie.services.userSvc.TokenService;
import com.bookmyshow.movie.services.userSvc.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }


    @GetMapping("/")
    public ResponseEntity<?> root(){
        return ResponseEntity.ok().body("Success");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO user){
        try {
            if (user == null) {
                return ResponseEntity.badRequest().body("Unable to register user");
            }
            System.out.println("User data received is "+user);
            MovieUser registeredUser = userService.register(user);
            if (registeredUser == null) {
                return ResponseEntity.badRequest().body("Unable to register user");
            }

            UserResponseDTO userResponseDTO = new UserResponseDTO(
                    registeredUser.getId(),
                    registeredUser.getUserName(),
                    registeredUser.getEmail(),
                    registeredUser.getDob(),
                    registeredUser.getCountry(),
                    registeredUser.getRoles().stream()
                            .map(MovieRole::getRoleName)
                            .collect(Collectors.toList())
            );
            return ResponseEntity.ok(userResponseDTO);
        } catch (Exception e) {
            System.out.println("Exception is "+e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> credentials, HttpServletRequest request){
        System.out.println("Creds are "+credentials);

        try{
            //Check if user is in the DB
            Token token = userService.login(credentials.get("username"), credentials.get("password"));

            return ResponseEntity.ok(token);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token){
        if(token==null ||token.isBlank()){
            return ResponseEntity.badRequest().body(false);
        }

        if(!token.startsWith("Bearer")){
            return ResponseEntity.badRequest().body(false);
        }

        token = token.substring(7);
        if(tokenService.deleteByAccessToken(token)){
            System.out.println("USer logged out");
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        }
        return ResponseEntity.badRequest().body("User token not found");
    }

    @GetMapping("/user")
        public ResponseEntity<?> getUser(@RequestHeader("Authorization") String Authorization){
        if(Authorization==null ||Authorization.isBlank()){
            return ResponseEntity.badRequest().body(false);
        }

        if(!Authorization.startsWith("Bearer")){
            return ResponseEntity.badRequest().body(false);
        }

        Authorization = Authorization.substring(7);
        MovieUser user = userService.getUser(Authorization);
        if(user!=null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO(
                    user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getDob(),
                    user.getCountry(),
                    user.getRoles().stream()
                            .map(MovieRole::getRoleName)
                            .collect(Collectors.toList())
            );
            System.out.println("Response user is "+userResponseDTO);
            return ResponseEntity.ok().body(userResponseDTO);
        }
        return ResponseEntity.badRequest().body(Map.of("message", "user not found"));
        }

}
