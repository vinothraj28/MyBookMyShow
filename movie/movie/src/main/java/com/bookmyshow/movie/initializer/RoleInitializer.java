package com.bookmyshow.movie.initializer;

import com.bookmyshow.movie.model.userCode.MovieRole;
import com.bookmyshow.movie.repository.userPack.RoleRepository;
import com.bookmyshow.movie.repository.userPack.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final TokenRepository tokenRepository;

    public RoleInitializer(RoleRepository roleRepository, TokenRepository tokenRepository) {
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void run(String... args) throws Exception {

    if(roleRepository.count() > 0){
        return;
    }




    System.out.println("Adding Role Admin and User");

    roleRepository.save(new MovieRole("ADMIN"));
    roleRepository.save(new MovieRole("USER"));

    }
}
