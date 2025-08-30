package com.bookmyshow.movie.model.MovieTheater;

import com.bookmyshow.movie.model.userCode.MovieUser;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"movieUser", "screen"})
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int screenCount;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "theater" , cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "theater-screens")
    private List<Screen> screen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference(value = "theater-users")
    @JoinColumn(name = "user_id")
    private MovieUser movieUser;


    public Theater(String name, int screenCount, MovieUser movieUser){
        this.name = name;
        this.screenCount = screenCount;
        this.movieUser = movieUser;
        screen = IntStream.rangeClosed(1, screenCount)
                .boxed().map(screen -> {
                    Screen s = new Screen("Screen_"+screen);
                    s.setTheater(this);
                    return  s;
                }
                )
                .collect(Collectors.toList());
    }

    public Theater(String name, int screenCount, List<Screen> screen, MovieUser movieUser) {
        this.name = name;
        this.screenCount = screenCount;
        this.screen = screen.stream().peek(s ->
                        s.setTheater(this))
                .collect(Collectors.toList());
        this.movieUser = movieUser;
    }
}
