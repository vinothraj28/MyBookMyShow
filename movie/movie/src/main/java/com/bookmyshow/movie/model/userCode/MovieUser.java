package com.bookmyshow.movie.model.userCode;


import com.bookmyshow.movie.model.MovieTheater.Theater;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"roles", "authCodes", "token", "theater"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MovieUser {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userName;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    private String password;
    private String country;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference(value = "user-roles")
    @JoinTable(
            name = "movie_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<MovieRole> roles = new HashSet<>();


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference(value = "user-codes")
    private Set<AuthorizationCode> authCodes;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-token")
    private Token token;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movieUser")
    @JsonBackReference(value = "theater-users")
    private Set<Theater> theater;

    public MovieUser(String userName, String email, Date dob, String password, String country, Set<MovieRole> roles) {
        this.userName = userName;
        this.email = email;
        this.dob = dob;
        this.password = password;
        this.country = country;
        this.roles = roles;
        this.token = null;
        this.authCodes = null;
    }
}
