package com.bookmyshow.movie.dto.MovieDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheaterDTO {

    private Long userId;
    private String name;
    private int screenCount;
    private List<ScreenDTO> screens;

}
