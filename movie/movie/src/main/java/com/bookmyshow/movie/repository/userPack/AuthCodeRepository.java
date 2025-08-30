package com.bookmyshow.movie.repository.userPack;

import com.bookmyshow.movie.model.userCode.AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepository extends JpaRepository<AuthorizationCode, Long> {
}
