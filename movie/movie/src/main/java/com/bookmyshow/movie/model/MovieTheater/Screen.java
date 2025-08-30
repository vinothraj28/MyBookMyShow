package com.bookmyshow.movie.model.MovieTheater;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.parallelSetAll;
import static java.util.Arrays.stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"showTimes", "theater"})
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private static final int Buffer_Time = 15;
    private static final LocalTime screenStartTime = LocalTime.of(9,0);
    private static final LocalTime screenEndTime = LocalTime.of(23,0);

    private int rows;
    private int columns;

    @ElementCollection
    @JsonIgnore
    private List<Seats> seats;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "screen")
    @JsonBackReference("screen-showTimes")
    private Set<ShowTimes> showTimes = new TreeSet<>();

    @ElementCollection
    private Set<LocalDate> fullBookedDates = new TreeSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "theater-screens")
    @JoinColumn(name = "theater_id")
    private Theater theater;

    public Screen(String name) {
        this.name = name;
    }

    public Screen(String name, int rows, int columns) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.seats = generateSeats(rows, columns);
    }

    public Screen(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        this.seats = generateSeats(rows, columns);
    }

    public void updateSeats(int rows, int columns){
        this.setRows(rows);
        this.setColumns(columns);
        this.setSeats(generateSeats(rows, columns));
    }

    private List<Seats> generateSeats(int rows, int columns) {
        return IntStream.range( 0, rows )
                .boxed()
                .flatMap(
                        row -> Stream.iterate(1, col -> col+1).limit(columns)
                                .map( col -> new Seats((char) ('A' + row) + String.valueOf(col), false))
                ).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<LocalDate> getAvaiableDates(){
        YearMonth thisMonth = YearMonth.now();
        int daysInMonth = thisMonth.lengthOfMonth();

        List<LocalDate> dates = IntStream.rangeClosed(1,daysInMonth)
                .boxed()
                .map(thisMonth::atDay)
                .filter(day -> fullBookedDates == null || !fullBookedDates.contains(day))
                .filter(day -> LocalDate.now().isBefore(day))
                .collect(Collectors.toList());
        return dates;
    }




}
