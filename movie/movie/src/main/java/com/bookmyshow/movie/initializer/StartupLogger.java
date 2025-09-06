package com.bookmyshow.movie.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("Backend container started successfully!");
    }
}
