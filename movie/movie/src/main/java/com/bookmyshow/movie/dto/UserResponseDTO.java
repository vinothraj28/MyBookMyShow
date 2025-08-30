package com.bookmyshow.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String userName;
    private String email;
    private Date dob;
    private String country;
    private List<String> roles = new ArrayList<>();
}
