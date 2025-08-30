package com.bookmyshow.movie.model.MovieTheater;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class ShowTimesList {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private LocalDate date;
//
//    @ElementCollection
//    private List<ShowTimes> showTimes;
//
//    public ShowTimesList(LocalDate date) {
//        this.date = date;
//        this.showTimes = new ArrayList<>();
//    }
//
//
//
//
//
//}
