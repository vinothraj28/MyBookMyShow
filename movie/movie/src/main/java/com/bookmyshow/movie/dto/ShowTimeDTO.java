package com.bookmyshow.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeDTO {

    private LocalTime showStartTime;
    private LocalTime showEndTime;
    private Long movieId;
    private Long screenId;
    private Long userId;
    private LocalDate date;

}
