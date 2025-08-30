package com.bookmyshow.movie.controller;

import com.bookmyshow.movie.dto.MovieDTO.*;
import com.bookmyshow.movie.model.MovieTheater.Screen;
import com.bookmyshow.movie.model.MovieTheater.ShowTimes;
import com.bookmyshow.movie.model.MovieTheater.Theater;
import com.bookmyshow.movie.model.userCode.MovieUser;
import com.bookmyshow.movie.services.movieSvc.ScreenService;
import com.bookmyshow.movie.services.movieSvc.ShowTimeService;
import com.bookmyshow.movie.services.movieSvc.TheaterService;
import com.bookmyshow.movie.services.userSvc.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/theater")
public class TheaterController {

    private final UserService userService;
    private final TheaterService theaterService;
    private final ScreenService screenService;
    private final ShowTimeService showTimeService;

    public TheaterController(UserService userService, TheaterService theaterService, ScreenService screenService, ShowTimeService showTimeService) {
        this.userService = userService;
        this.theaterService = theaterService;
        this.screenService = screenService;
        this.showTimeService = showTimeService;
    }

    @PostMapping("/registerHalf")
    public ResponseEntity<?> register(@RequestBody Map<String, String> theater){
        String theaterName = theater.get("name");
        int screenCount = Integer.parseInt(theater.get("screenCount"));
        Long userId = Long.parseLong(theater.get("userId"));
        MovieUser movieUser = userService.findById(userId);
        Theater theater1 = new Theater(theaterName, screenCount, movieUser);
        return ResponseEntity.ok().body(theaterService.save(theater1));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerFull(@RequestBody TheaterDTO theaterDTO){
        System.out.println("Received theater values "+theaterDTO);
        //Extract the values
        String theaterName = theaterDTO.getName();
        int screenCount = theaterDTO.getScreenCount();
        Long userId = theaterDTO.getUserId();

        //Create th List

        List<Screen> screens = theaterDTO.getScreens().stream().map( screen ->
                new Screen( screen.getScreenName(), screen.getRows(),screen.getColumns() )
        )
                .collect(Collectors.toList());

        MovieUser movieUser = userService.findById(userId);

        Theater theater1 = new Theater(theaterName, screenCount, screens, movieUser);
        return ResponseEntity.ok().body(theaterService.save(theater1));
    }

    @PostMapping("/list")
    public ResponseEntity<?> getTheaters(@RequestBody Map<String, String> user ){
        Long userId = Long.parseLong( user.get("userId"));

        List<Theater> theater = theaterService.findByMovieUser_Id(userId);
        List <TheaterListResponseDTO> responseDTOS = theater.stream()
                .map( theater1 -> new TheaterListResponseDTO( theater1.getId(),
                        theater1.getName(),
                        theater1.getScreenCount()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responseDTOS );
    }

    @PostMapping("/screen/update")
    public ResponseEntity<?> updateScreen(@RequestBody ScreenDTO screenDTO){
        Long screenId = screenDTO.getScreenId();
        int rows = screenDTO.getRows();
        int columns = screenDTO.getColumns();
        Long userId = screenDTO.getUserId();

        Screen screen1 = screenService.updateScreen(screenId, rows, columns, userId);
        if(screen1!=null)
            return ResponseEntity.ok().body(screen1);

        return ResponseEntity.badRequest().body("Unable to update the screen");
    }

//    @PostMapping("/book/seats")
//    public ResponseEntity<?> bookSeats(@RequestBody BookingRequest bookingRequest){
//        ShowTimes showTimes = showTimeService.bookSeats(bookingRequest.getShowTimeId(), bookingRequest.getSeatNumbers());
//        if(showTimes != null)
//            return ResponseEntity.ok().body(showTimes);
//        return ResponseEntity.badRequest().body("Unable to book seats. Try Again");
//    }

    @GetMapping("/screen/{id}/seats")
    public ResponseEntity<?> getSeats(@PathVariable Long id){
        return ResponseEntity.ok().body(screenService.getSeats(id));
    }

    @GetMapping("/{id}/screens")
    public ResponseEntity<?> getScreens(@PathVariable Long id){
        Theater theater = theaterService.findById(id);

        TheaterResponseDTO theaterResponseDTO = new TheaterResponseDTO(
            theater.getId(), theater.getName(), theater.getScreenCount(), theater.getScreen()
    );

        return ResponseEntity.ok().body(theaterResponseDTO);
    }

}
