package com.bookmyshow.movie.services.movieSvc;

import com.bookmyshow.movie.model.MovieTheater.Theater;
import com.bookmyshow.movie.repository.moviePack.TheaterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    public Theater save(Theater theater) {
       return theaterRepository.save(theater);
    }

    public List<Theater> findByMovieUser_Id(Long userId) {
        return theaterRepository.findByMovieUser_Id(userId);
    }

    public List<Theater> getScreensByID(Long id) {
        return theaterRepository.getScreensById(id);
    }

    public Theater findById(Long id) {
        return theaterRepository.findById(id).get();
    }
}
