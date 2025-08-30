package com.bookmyshow.movie.controller;

import com.bookmyshow.movie.dto.MovieDTO.MovieResponseDTO;
import com.bookmyshow.movie.dto.MovieDTO.MovieShowsDTO;
import com.bookmyshow.movie.dto.MovieDTO.RegisterMovieDTO;
import com.bookmyshow.movie.dto.ShowTimeDTO;
import com.bookmyshow.movie.model.MovieTheater.Movie;
import com.bookmyshow.movie.model.images.MovieImage;
import com.bookmyshow.movie.services.movieSvc.MovieImageService;
import com.bookmyshow.movie.services.movieSvc.MovieService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieImageService movieImageService;

    public MovieController(MovieService movieService, MovieImageService movieImageService) {
        this.movieService = movieService;
        this.movieImageService = movieImageService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> addMovie(@RequestPart RegisterMovieDTO registerMovieDTO,
                                      @RequestPart MultipartFile multipartFile) throws IOException {


        MovieImage movieImage = new MovieImage(multipartFile.getOriginalFilename(),multipartFile.getContentType() , multipartFile.getBytes());
        System.out.println(multipartFile.getOriginalFilename()+" "+multipartFile.getContentType()+" ");
        Movie movie = new Movie(registerMovieDTO.getName(), registerMovieDTO.getRating(), registerMovieDTO.getMovieHrs()
        ,registerMovieDTO.getLanguage(), movieImage);

        System.out.println(registerMovieDTO+" "+multipartFile);

        return ResponseEntity.ok().body(movieService.save(movie));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getMovies(){
        List<MovieResponseDTO> movieResponseDTO=  movieService.findAll().stream()
                .map( movie -> new MovieResponseDTO(movie.getId(), movie.getName(), movie.getRating(), movie.getLanguage(),movie.getMovieHrs() ,
                        movie.getMovieImage() !=null ? movie.getMovieImage().getId() : null ))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(movieResponseDTO);
    }

    @GetMapping("/image/{id}")
    public  ResponseEntity<?> getMovieImage(@PathVariable Long id){
        if(id!=null){
            MovieImage movieImage = movieImageService.getMovieImage(id);
            byte[] imgBytes = movieImage.getImageBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType( Objects.equals(movieImage.getType(),"image/jpeg")? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG);
            headers.setContentLength(imgBytes.length);
            return ResponseEntity.ok().headers(headers).body(imgBytes);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{movieId}/showTimes")
    public ResponseEntity<?> getShowsByMovie(@PathVariable Long movieId){
        System.out.println("Retrieved ShowTimes are"+movieService.getShowsByMovie(movieId));
        List<MovieShowsDTO> movieShows = movieService.getShowsByMovie(movieId).stream()
                .map(s ->  new MovieShowsDTO(s.getId(),
                        s.getDate(), s.getStartTime(), s.getEndTime()))
                        .toList();
        return ResponseEntity.ok().body(movieShows);
    }
}
