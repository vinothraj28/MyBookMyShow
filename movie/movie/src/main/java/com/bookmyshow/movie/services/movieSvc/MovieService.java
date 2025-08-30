package com.bookmyshow.movie.services.movieSvc;

import com.bookmyshow.movie.model.MovieTheater.Movie;
import com.bookmyshow.movie.model.MovieTheater.ShowTimes;
import com.bookmyshow.movie.repository.moviePack.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie save(Movie movie){
        return movieRepository.save(movie);
    }

    public Movie findById(Long movieId) {
        return movieRepository.findById(movieId).get();
    }

    @Transactional
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Transactional
    public List<ShowTimes> getShowsByMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).get();
        return new ArrayList<>(movie.getShowTimesList());
    }
}
