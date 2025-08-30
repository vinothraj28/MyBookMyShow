package com.bookmyshow.movie.dto.MovieDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterListResponseDTO {

    private Long id;
    private String name;
    private  int screenCount;


}
