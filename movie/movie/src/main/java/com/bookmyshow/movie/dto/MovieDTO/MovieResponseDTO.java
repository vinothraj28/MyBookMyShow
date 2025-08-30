package com.bookmyshow.movie.dto.MovieDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponseDTO {

    private Long id;
    private String name;
    private String  rating;
    private String language;
    private int movieHrs;
    private Long movieImageId;

}
