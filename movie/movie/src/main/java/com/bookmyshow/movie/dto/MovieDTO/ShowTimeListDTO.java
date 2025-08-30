package com.bookmyshow.movie.dto.MovieDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeListDTO {

    private long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private String screenName;
    private String TheaterName;

}
