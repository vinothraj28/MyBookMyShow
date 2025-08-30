package com.bookmyshow.movie.dto.MovieDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMovieDTO {

    private String name;
    private String rating;
    private String Language;
    private int movieHrs;

}
