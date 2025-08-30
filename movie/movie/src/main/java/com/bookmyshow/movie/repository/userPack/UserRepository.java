package com.bookmyshow.movie.repository.userPack;

import com.bookmyshow.movie.model.userCode.MovieUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MovieUser, Long> {
    Optional<MovieUser> findByUserName(String username);
}
