package com.bookmyshow.movie.repository.moviePack;

import com.bookmyshow.movie.model.MovieTheater.ShowTimes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTimes, Long> {

    List<ShowTimes> findByScreen_IdAndDate(Long screenId, LocalDate date);

    List<ShowTimes> findShowTimesByMovie_Id(Long movieId);
}
