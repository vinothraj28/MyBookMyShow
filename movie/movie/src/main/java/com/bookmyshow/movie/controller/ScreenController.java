package com.bookmyshow.movie.controller;

import com.bookmyshow.movie.dto.ShowTimeDTO;
import com.bookmyshow.movie.model.MovieTheater.Movie;
import com.bookmyshow.movie.model.MovieTheater.Screen;
import com.bookmyshow.movie.repository.moviePack.ScreenRepository;
import com.bookmyshow.movie.services.movieSvc.MovieService;
import com.bookmyshow.movie.services.movieSvc.ScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/screen")
public class ScreenController {


    private final ScreenService screenService;
    private final MovieService movieService;


    public ScreenController(ScreenRepository screenRepository, ScreenService screenService, MovieService movieService) {

        this.screenService = screenService;
        this.movieService = movieService;
    }

    @GetMapping("/{id}/showtimes/{duration}")
    public ResponseEntity<?> getScreenShowTimes(@PathVariable Long id, @PathVariable int duration,
                                                @RequestParam LocalDate date ){
        return ResponseEntity.ok().body(screenService.getScreenShowTimes(id, duration, date));
    }

    @PostMapping("/showtime/available")
    public ResponseEntity<?> getShowTimes(@RequestParam Long screenId, @RequestParam LocalDate date, @RequestParam int duration){
       return ResponseEntity.ok().body(screenService.getScreenShowTimes(screenId, duration, date));
    }

    /*
    @PostMapping("/showtimes/add")
    public ResponseEntity<?> addShowTimes(@RequestBody ShowTimeDTO showTimeDTO
                                          ){
        Movie movie = movieService.findById(showTimeDTO.getMovieId());
        Screen screen = screenService.findById(showTimeDTO.getScreenId());
        screen.addShowTime(movie, showTimeDTO.getShowStartTime(), showTimeDTO.getShowEndTime(), showTimeDTO.getDate());
        screen = screenService.save(screen);
        return ResponseEntity.badRequest().body(screen);

    } */

    @GetMapping("/{screenId}/dates")
    public ResponseEntity<?> getAvaiableDates(@PathVariable Long screenId ){
        Screen screen = screenService.findById(screenId);
        return ResponseEntity.ok().body(screen.getAvaiableDates());
    }





}
