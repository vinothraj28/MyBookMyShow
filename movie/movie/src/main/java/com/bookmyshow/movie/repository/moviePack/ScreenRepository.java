package com.bookmyshow.movie.repository.moviePack;

import com.bookmyshow.movie.model.MovieTheater.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
}
