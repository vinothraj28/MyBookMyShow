package com.bookmyshow.movie.model.userCode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuthorizationCode {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String authCode;

    @ManyToOne
    @JsonBackReference(value = "user-codes")
    @JoinColumn(name = "user_id")
    private MovieUser user;

}
