package com.bookmyshow.movie.dto.MovieDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenDTO {

    private Long screenId;
    private String screenName;
    private int rows;
    private int columns;
    private Long userId;

    public ScreenDTO(Long screenId, String screenName, int rows, int columns) {
        this.screenId = screenId;
        this.screenName = screenName;
        this.rows = rows;
        this.columns = columns;
    }
}
