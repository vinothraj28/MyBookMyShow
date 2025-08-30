package com.bookmyshow.movie.model.MovieTheater;


import com.bookmyshow.movie.model.images.MovieImage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Movie {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String language;

    private int movieHrs;

    private String rating;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
    @JsonBackReference(value="showTime-movies")
    private Set<ShowTimes> showTimesList;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "image_id")
    private MovieImage movieImage;

    public Movie(String name, String rating, int movieHrs, String language, MovieImage movieImage) {
        this.name = name;
        this.rating = rating;
        this.movieHrs = movieHrs;
        this.language = language;
        this.movieImage = movieImage;
    }


}
