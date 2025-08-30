package com.bookmyshow.movie.repository.userPack;

import com.bookmyshow.movie.model.userCode.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByRefreshToken(String token);

    void deleteByRefreshToken(String token);

    int deleteByAccessToken(String token);

    Token findByAccessToken(String token);
}
