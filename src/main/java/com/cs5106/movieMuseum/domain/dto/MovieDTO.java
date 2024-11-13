package com.cs5106.movieMuseum.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieDTO {
    private Long id;
    private String title;
    private int releaseYear;
    private double imdbRating;

    public MovieDTO(Long id, String title, int releaseYear, double imdbRating) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.imdbRating = imdbRating;
    }
}
