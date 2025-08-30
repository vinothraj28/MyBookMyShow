package com.bookmyshow.movie.services.movieSvc;

import com.bookmyshow.movie.model.images.MovieImage;
import com.bookmyshow.movie.repository.moviePack.MovieImageRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MovieImageService {

    private final MovieImageRepo movieImageRepo;

    public MovieImageService(MovieImageRepo movieImageRepo) {
        this.movieImageRepo = movieImageRepo;
    }

    @Transactional
    public MovieImage getMovieImage(Long id){

        MovieImage movieImage = movieImageRepo.findById(id).get();
        return movieImage;
    }

}
