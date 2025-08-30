package com.bookmyshow.movie.repository.moviePack;

import com.bookmyshow.movie.model.MovieTheater.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    List<Theater> findByMovieUser_Id(Long userId);

    List<Theater> getScreensById(Long id);
}
