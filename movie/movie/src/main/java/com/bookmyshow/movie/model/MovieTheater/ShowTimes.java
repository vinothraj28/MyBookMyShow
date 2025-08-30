package com.bookmyshow.movie.model.MovieTheater;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"movie", "screen", "showTimeSeats"})
public class ShowTimes {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDate date;


    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference("showTime-movies")
    @JoinColumn(name="movie-id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference("screen-showTimes")
    @JoinColumn(name = "screen-id")
    private Screen screen;

    @ElementCollection
    private List<Seats> showTimeSeats;

    public ShowTimes(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean bookSeats(List<Seats> seatNumber){
        boolean atleastOneBooked = false;
        for(Seats seats : this.getShowTimeSeats()){
            for(Seats seat : seatNumber){
                if(seats.getSeatNumber().equals(seat.getSeatNumber())){
                    seats.setBooked(true);
                    seats.setUserName(seat.getUserName());
                    atleastOneBooked = true;
                }
            }
        }
        return atleastOneBooked;
    }

    public ShowTimes(LocalTime startTime, LocalTime endTime, Movie movie, Screen screen, List<Seats> showTimeSeats, LocalDate date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.movie = movie;
        this.screen = screen;
        this.showTimeSeats = showTimeSeats;
        this.date = date;
    }



}
