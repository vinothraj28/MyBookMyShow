package com.bookmyshow.movie.services.userSvc;

import com.bookmyshow.movie.jwtoken.JwUtil;
import com.bookmyshow.movie.dto.RoleDTO;
import com.bookmyshow.movie.dto.UserRequestDTO;
import com.bookmyshow.movie.exceptions.UserNotFoundException;
import com.bookmyshow.movie.model.userCode.MovieUser;
import com.bookmyshow.movie.model.userCode.Token;
import com.bookmyshow.movie.repository.userPack.RoleRepository;
import com.bookmyshow.movie.repository.userPack.TokenRepository;
import com.bookmyshow.movie.repository.userPack.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final JwUtil jwUtil;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, JwUtil jwUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.jwUtil = jwUtil;
    }

    public MovieUser register(UserRequestDTO user) {

        MovieUser movieUser = new MovieUser(
                user.getUserName(),
                user.getEmail(),
                user.getDob(),
                passwordEncoder.encode(user.getPassword()),
                user.getCountry(),
                user.getRoles().stream()
                .map(RoleDTO::getRoleName).
                        map(roleRepository::findByRoleName)
                        .collect(Collectors.toSet())

        );
        System.out.println("In USer Service "+movieUser);

        return userRepository.save(movieUser);
    }

    public MovieUser findByUserName(String username) {
        Optional<MovieUser> user = userRepository.findByUserName(username);
        return user.orElseThrow(() -> new UserNotFoundException(username));
    }

    public Token login(String username, String password) throws CredentialException {
        Optional<MovieUser> user = userRepository.findByUserName(username);

        if(user.isPresent()){
            if(passwordEncoder.matches(password, user.get().getPassword())){

                //Create Token for the user
                Token token = new Token();
                token.setUser(user.get());

                token.setAccessToken(jwUtil.generateToken(username,
                        new ArrayList<>(user.get().getRoles())));
                token.setRefreshToken( jwUtil.generateRefreshToken(username) );

                //save the token
                Token savedToken = tokenRepository.save(token);

                //update the user with token
                user.get().setToken(savedToken);
                userRepository.save(user.get());

                return savedToken;
            }else{
                throw new CredentialException("password incorrect");
            }
        } else{
            throw new UserNotFoundException(username);
        }
    }

    public MovieUser findById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public MovieUser getUser(String authorization) {
        if(jwUtil.validate(authorization)){
            Claims claims = jwUtil.extractClaims(authorization);
            String username = claims.getSubject();

            return userRepository.findByUserName(username).get();
        }
        return null;

    }
}
