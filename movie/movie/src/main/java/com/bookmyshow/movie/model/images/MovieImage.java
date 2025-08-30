package com.bookmyshow.movie.model.images;


import com.bookmyshow.movie.model.MovieTheater.Movie;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "movie")
public class MovieImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;

    @Lob
    private byte[] imageBytes;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "movieImage")
    @JsonBackReference
    private Movie movie;

    public MovieImage(String name, String type, byte[] imageBytes) {
        this.name = name;
        this.type = type;
        this.imageBytes = imageBytes;
    }
}
