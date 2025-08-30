package com.bookmyshow.movie.services.movieSvc;

import com.bookmyshow.movie.model.MovieTheater.Movie;
import com.bookmyshow.movie.model.MovieTheater.Screen;
import com.bookmyshow.movie.model.MovieTheater.Seats;
import com.bookmyshow.movie.model.MovieTheater.ShowTimes;
import com.bookmyshow.movie.repository.moviePack.MovieRepository;
import com.bookmyshow.movie.repository.moviePack.ScreenRepository;
import com.bookmyshow.movie.repository.moviePack.ShowTimeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowTimeService {


    private final ShowTimeRepository showTimeRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;

    @Autowired
    public ShowTimeService(ShowTimeRepository showTimeRepository, MovieRepository movieRepository, ScreenRepository screenRepository) {
        this.showTimeRepository = showTimeRepository;
        this.movieRepository = movieRepository;
        this.screenRepository = screenRepository;
    }

    @Transactional
    public ShowTimes save(Long movieId, Long  screenId, LocalTime startTime, LocalTime endTime, Long userId, LocalDate date){
        Movie movie = movieRepository.findById(movieId).get();
        Screen screen = screenRepository.findById(screenId).get();

        if(userId.equals(screen.getTheater().getMovieUser().getId())) {
            // Clone seats for this showtime
            List<Seats> clonedSeats = new ArrayList<>();
            for (Seats seat : screen.getSeats()) {
                Seats clonedSeat = new Seats();
                clonedSeat.setSeatNumber(seat.getSeatNumber());
                clonedSeat.setBooked(false);
                clonedSeats.add(clonedSeat);
            }

            ShowTimes showTimes = new ShowTimes(startTime, endTime, movie, screen, clonedSeats, date);
            ShowTimes showTimes1 = showTimeRepository.save(showTimes);

            screen.getShowTimes().add(showTimes1);
            screenRepository.save(screen);

            return showTimes1;
        }
        return null;
    }

    public ShowTimes bookSeats(Long showTimeId, List<Seats> seats) {
        ShowTimes showTimes = null;

        if(showTimeRepository.findById(showTimeId).isPresent()){
            showTimes =  showTimeRepository.findById(showTimeId).get();
            if(showTimes.bookSeats(seats))
                return showTimeRepository.save(showTimes);
        }
        return showTimes;
    }

    public ShowTimes findById(Long showTimeId) {
        return showTimeRepository.findById(showTimeId).get();
    }

    @Transactional
    public List <ShowTimes> findShowsByMovieId(Long movieId) {
        return showTimeRepository.findShowTimesByMovie_Id(movieId);
    }
}
