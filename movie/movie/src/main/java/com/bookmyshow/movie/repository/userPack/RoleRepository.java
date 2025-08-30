package com.bookmyshow.movie.repository.userPack;

import com.bookmyshow.movie.model.userCode.MovieRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<MovieRole, Long> {
    MovieRole findByRoleName(String roleName);
}
