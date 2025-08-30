package com.bookmyshow.movie.dto.MovieDTO;

import com.bookmyshow.movie.model.MovieTheater.Seats;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatsDTO {

    private Long showTimeID;
    private List<Seats> seats;
}
