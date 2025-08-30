package com.bookmyshow.movie.services.userSvc;

import com.bookmyshow.movie.exceptions.UserNotFoundException;
import com.bookmyshow.movie.jwtoken.JwUtil;
import com.bookmyshow.movie.model.userCode.MovieRole;
import com.bookmyshow.movie.model.userCode.MovieUser;
import com.bookmyshow.movie.model.userCode.Token;
import com.bookmyshow.movie.repository.userPack.TokenRepository;
import com.bookmyshow.movie.repository.userPack.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.util.List;


@Service
public class TokenService {

    private final JwUtil jwUtil;

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public TokenService(JwUtil jwUtil, TokenRepository tokenRepository, UserRepository userRepository) {
        this.jwUtil = jwUtil;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }


    public Claims introspect(String token) {

        return jwUtil.extractClaims(token);

    }

    public Boolean isTokenValid(String token){
        return jwUtil.validate(token);
    }



    public Token refresh(String token) throws CredentialException {

        if(!jwUtil.validate(token))
            throw new CredentialException("token is invalid");
        if(tokenRepository.findByRefreshToken(token) == null)
            throw new CredentialException("token not found");

        Token existingToken = tokenRepository.findByRefreshToken(token);
        String accessToken = existingToken.getAccessToken();

        MovieUser tokenUser = userRepository.findByUserName(existingToken.getUser().getUserName())
                .orElseThrow(()->new UserNotFoundException("User not found"));
        existingToken.setUser(null);
        tokenUser.setToken(null);
        tokenRepository.deleteById(existingToken.getId());

        Claims claims = jwUtil.extractClaims(accessToken);
        String username = claims.getSubject();
        // Safely parse roles from claim
        List<MovieRole> roles = claims.get("roles", List.class);

        //Create the new refresh token, attach it to the user and save it
        Token refreshToken = new Token();
        refreshToken.setRefreshToken(jwUtil.generateRefreshToken(username));
        refreshToken.setAccessToken(jwUtil.generateToken(username, roles));
        refreshToken.setUser(tokenUser);
        tokenRepository.save(refreshToken);

        //attach new token to the user
        tokenUser.setToken(refreshToken);
        userRepository.save(tokenUser);

        return tokenRepository.findByRefreshToken(refreshToken.getRefreshToken());
    }

    @Transactional
    public boolean deleteByAccessToken(String token) {
        Token aToken = tokenRepository.findByAccessToken(token);

        if(aToken!= null){
            Long movieUserId = aToken.getUser().getId();
            MovieUser movieUser =  userRepository.findById(movieUserId).get();
            movieUser.setToken(null);
            userRepository.save(movieUser);
            tokenRepository.deleteById(aToken.getId());
            return true;
        }
        return false;

    }
}
