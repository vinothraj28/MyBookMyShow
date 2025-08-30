package com.bookmyshow.movie.repository.moviePack;

import com.bookmyshow.movie.model.MovieTheater.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
