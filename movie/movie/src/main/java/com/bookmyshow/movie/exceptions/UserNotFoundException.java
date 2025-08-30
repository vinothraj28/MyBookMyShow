package com.bookmyshow.movie.exceptions;

public class UserNotFoundException extends RuntimeException{

    private String username;

    public UserNotFoundException(String username){
        super("User not found: " + username);
    }
}
