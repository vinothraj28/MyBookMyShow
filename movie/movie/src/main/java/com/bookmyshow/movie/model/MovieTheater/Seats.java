package com.bookmyshow.movie.model.MovieTheater;

import com.bookmyshow.movie.dto.MovieDTO.SeatsDTO;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seats {

    private String seatNumber;
    private boolean isBooked;
    private String userName;

    public Seats(String seatNumber, boolean isBooked) {
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
    }
}
