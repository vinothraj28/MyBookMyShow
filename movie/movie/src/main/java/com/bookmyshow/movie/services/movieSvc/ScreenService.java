package com.bookmyshow.movie.services.movieSvc;

import com.bookmyshow.movie.model.MovieTheater.Movie;
import com.bookmyshow.movie.model.MovieTheater.Screen;
import com.bookmyshow.movie.model.MovieTheater.Seats;
import com.bookmyshow.movie.model.MovieTheater.ShowTimes;
import com.bookmyshow.movie.repository.moviePack.ScreenRepository;
import com.bookmyshow.movie.repository.moviePack.ShowTimeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final ShowTimeRepository showTimeRepository;

    public ScreenService(ScreenRepository screenRepository, ShowTimeRepository showTimeRepository) {
        this.screenRepository = screenRepository;
        this.showTimeRepository = showTimeRepository;
    }

    public Screen save(Screen screen){
        return screenRepository.save(screen);
    }

    public Screen updateScreen(Long screenId, int rows, int columns, Long userId) {
        Screen screen=null;
       if(screenRepository.findById(screenId).isPresent()){
           System.out.println("Screen has been found");
           screen = screenRepository.findById(screenId).get();
           System.out.println("Screen value while updating is "+screen);
           if(screen.getTheater().getMovieUser().getId().equals(userId)){
               screen.updateSeats(rows, columns);
           }
       }
        if(screen!=null && screen.getId()!=null){
            return screenRepository.save(screen);
       }
        return screen;
    }

    public List<Seats> getSeats(Long id) {
        List<Seats> seats = null;
        if(screenRepository.findById(id).isPresent()){
            return screenRepository.findById(id).get().getSeats();
        }
        return seats;
    }

    @Transactional
    public List<ShowTimes> getScreenShowTimes(Long id, int movieDuration, LocalDate date) {

        if(screenRepository.findById(id).isPresent()){
            Screen screen = screenRepository.findById(id).get();
            Long screenId = screen.getId();
            List<ShowTimes> showTimes = showTimeRepository.findByScreen_IdAndDate(screenId,date);
            System.out.println(showTimes);
            //System.out.println("getScreenShowTimes CALLED at " + LocalDateTime.now() + " with screenId=" + screenId);
            return getAvailableSlots(movieDuration, date, showTimes);
        }
        return new ArrayList<>();
    }

    public List<ShowTimes> getAvailableSlots(int movieDurationInMinutes, LocalDate date, List<ShowTimes> showTimes){

         final int Buffer_Time = 15;
         final LocalTime screenStartTime = LocalTime.of(9,0);
         final LocalTime screenEndTime = LocalTime.of(23,0);

        LocalTime nextStart = screenStartTime;
        List<ShowTimes> availableSlots = new ArrayList<>();
        showTimes.sort(Comparator.comparing(ShowTimes::getStartTime));

        //Shows slots available between the existing shows
        for(ShowTimes sh : showTimes){
            LocalTime lastEnd = sh.getStartTime();
            while(Duration.between(nextStart, lastEnd).toMinutes() >= movieDurationInMinutes + Buffer_Time){
                availableSlots.add(new ShowTimes(nextStart, nextStart.plusMinutes(movieDurationInMinutes+Buffer_Time)));
                nextStart = nextStart.plusMinutes(movieDurationInMinutes+Buffer_Time);
            }
            nextStart = sh.getEndTime();
        }
        //If still slots available till the screen end time
        while(Duration.between(nextStart, screenEndTime ).toMinutes() >=
                (movieDurationInMinutes + Buffer_Time)){
            availableSlots.add(new ShowTimes(nextStart,
                    nextStart.plusMinutes(movieDurationInMinutes+Buffer_Time)));
            nextStart = nextStart.plusMinutes(movieDurationInMinutes+Buffer_Time);
        }
        return availableSlots;
    }


    @Transactional
    public Screen findById(Long screenId) {
        if (screenRepository.findById(screenId).isPresent()) {
            Screen screen = screenRepository.findById(screenId).get();
            return screen;
        }
        return null;
    }

}
