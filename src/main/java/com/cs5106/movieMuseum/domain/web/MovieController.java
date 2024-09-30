package com.cs5106.movieMuseum.domain.web;

import com.cs5106.movieMuseum.domain.Movie;
import com.cs5106.movieMuseum.domain.repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {
    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping(value = "/movie")
    public Iterable<Movie> getMovies() {
        return movieRepository.findAll();
    }
}
