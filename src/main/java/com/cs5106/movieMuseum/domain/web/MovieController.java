package com.cs5106.movieMuseum.domain.web;

import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.MovieRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class MovieController {
    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Consider error handling? (null values mar shampla)
    // To-Do TESTING!!!!!!!!!!!!!!!!!!!!!!
    // Get request for all movies returns iterable list
    @GetMapping(value = "/movies")
    public Iterable<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @PutMapping(value = "/movies")
    public void updateMovie(@RequestBody Movie movie) {
        movieRepository.save(movie);
    }

    @GetMapping(value = "/movies")
    public Iterable<Movie> getMovieByTitle(@RequestParam("title") String title) {
        return movieRepository.findByTitle(title);
    }

    @GetMapping(value = "/movies")
    public Iterable<Movie> getMovieByReleaseYear(@RequestParam("releaseYear") int releaseYear) {
        return movieRepository.findByReleaseYear(releaseYear);
    }

    @GetMapping(value = "/movies")
    public Iterable<Movie> getMovieByImdbRating(@RequestParam("imdbRating") int imdbRating) {
        return movieRepository.findByImdbRating(imdbRating);
    }

    @GetMapping(value = "/movies")
    public Iterable<Movie> getMovieByTitleAndImdbRatingOrderByImdbRatingDesc(@RequestParam("title") String title, @RequestParam("imdbRating") int imdbRating) {
        return movieRepository.findByTitleAndImdbRatingOrderByImdbRatingDesc(title, imdbRating);
    }

    @PostMapping(value = "/movies")
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    @DeleteMapping(value = "/movies")
    public void deleteMovie(@RequestBody Movie movie) {
        movieRepository.delete(movie);
    }
}