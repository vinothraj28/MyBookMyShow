package com.bookmyshow.movie.repository.moviePack;

import com.bookmyshow.movie.model.images.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieImageRepo extends JpaRepository<MovieImage, Long> {
}
