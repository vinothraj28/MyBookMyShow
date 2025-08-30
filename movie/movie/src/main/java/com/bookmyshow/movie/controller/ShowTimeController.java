package com.bookmyshow.movie.controller;

import com.bookmyshow.movie.dto.MovieDTO.SeatsDTO;
import com.bookmyshow.movie.dto.MovieDTO.ShowTimeListDTO;
import com.bookmyshow.movie.dto.ShowTimeDTO;
import com.bookmyshow.movie.model.MovieTheater.Seats;
import com.bookmyshow.movie.model.MovieTheater.ShowTimes;
import com.bookmyshow.movie.services.movieSvc.ShowTimeService;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/showtime")
public class ShowTimeController {

    private final ShowTimeService showTimeService;

    public ShowTimeController(ShowTimeService showTimeService) {
        this.showTimeService = showTimeService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ShowTimeDTO showTimeDTO){
        Long movieId = showTimeDTO.getMovieId();
        Long screenId = showTimeDTO.getScreenId() ;
        LocalTime startTime = showTimeDTO.getShowStartTime();
        LocalTime endTime = showTimeDTO.getShowEndTime();
        Long userId = showTimeDTO.getUserId();
        LocalDate date = showTimeDTO.getDate();

        System.out.println("Received date value is "+date+" movie id is "+movieId+" screen id is "+screenId);

        return ResponseEntity.ok().body(showTimeService.save(movieId, screenId , startTime, endTime, userId, date));
    }

    @GetMapping("/{showTimeId}")
    public ResponseEntity<?> getShowTimes(@PathVariable Long showTimeId){
        return ResponseEntity.ok().body(showTimeService.findById(showTimeId));
    }

    @GetMapping("/seats/{showTimeId}")
    public ResponseEntity<?> getShowSeats(@PathVariable Long showTimeId){
        return ResponseEntity.ok().body(showTimeService.findById(showTimeId).getShowTimeSeats());
    }

    @GetMapping("/shows/{movieId}")
    public ResponseEntity<?> getShowsByMovie(@PathVariable Long movieId){
        List<ShowTimes> showTimesList = showTimeService.findShowsByMovieId(movieId);
        if(!showTimesList.isEmpty()){
            List<ShowTimeListDTO> showTimesListDTOS= showTimesList.stream().map(s -> new ShowTimeListDTO(
                    s.getId(), s.getStartTime(), s.getEndTime(), s.getDate(), s.getScreen().getName(), s.getScreen().getTheater().getName()
            )).toList();
            return ResponseEntity.ok().body(showTimesListDTOS);
        }
        return ResponseEntity.ok().body("Shows not Found");
    }

    @PostMapping("/book/tickets")
    public ResponseEntity<?> bookTicktes(@RequestBody SeatsDTO seatsDTO){

        Long showTimeId = seatsDTO.getShowTimeID();
        List<Seats> seats = seatsDTO.getSeats();

        return ResponseEntity.ok().body(showTimeService.bookSeats(showTimeId, seats));

    }

    @PostMapping("/download/tickets")
    public ResponseEntity<?> downloadTickets(@RequestBody SeatsDTO seatsDTO){

       // For Text File
//        String fileContent = "Booking Details:\n" +
//                "User: " + seatsDTO.getSeats().getFirst().getUserName() + "\n" +
//                "Seats: " + seatsDTO.getSeats().stream().map(Seats::getSeatNumber).toList();
//
//        byte[] fileBytes = fileContent.getBytes(StandardCharsets.UTF_8);
//        // 3. Set headers for download
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF); // or MediaType.APPLICATION_PDF
//        headers.setContentDisposition(
//                ContentDisposition.builder("attachment")
//                        .filename("booking.txt") // filename user will see
//                        .build()
//        );

         //1. Create PDF in memory
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Booking Details"));
        document.add(new Paragraph("User: " + seatsDTO.getSeats().get(0).getUserName()));
        document.add(new Paragraph("Seats: " + seatsDTO.getSeats().stream()
                .map(Seats::getSeatNumber)
                .collect(Collectors.joining(", "))));

        document.close();

        byte[] pdfBytes = out.toByteArray();

        // 2. Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename("booking.pdf")
                        .build()
        );


      return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

  }

}
