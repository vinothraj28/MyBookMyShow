package com.bookmyshow.movie.dto.MovieDTO;

import com.bookmyshow.movie.model.MovieTheater.Screen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheaterResponseDTO {

    private Long id;
    private String theaterName;
    private int screenCount;
    private List<Screen> screens;
}
