package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Movie;

import com.cs5106.movieMuseum.domain.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {
    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping("/movies")
    public Iterable<Movie> getMovies() {
        return service.getMovies();
    }

    @GetMapping("/movie/title/{title}")
    public Movie getMovieByTitle(@PathVariable String title) {
        return service.getMovieByTitle(title);
    }

    @GetMapping("/movies/releaseYear/{releaseYear}")
    public List<Movie> getMoviesByReleaseYear(@PathVariable int releaseYear) {
        return service.getMoviesByReleaseYear(releaseYear);
    }

    @GetMapping("/movies/imdbRating/{imdbRating}")
    public List<Movie> getMoviesByImdbRating(@PathVariable double imdbRating) {
        return service.getMoviesByImdbRating(imdbRating);
    }

    @GetMapping("movies/imdbRating/between/{low}/{high}")
    public List<Movie> getMoviesByImdbRatingBetween(@PathVariable double low, @PathVariable double high) {
        return service.getMoviesByImdbRatingBetween(low, high);
    }

    @GetMapping("/movies/title/substring/{title}")
    public List<Movie> getMoviesByTitleSubstring(@PathVariable String title) {
        return service.getMoviesByTitleSubstring(title);
    }

    @PostMapping("/movie/post")
    public Movie addMovie(@RequestBody Movie movie) {
        return service.addMovie(movie);
    }

    @PutMapping("/movies/put")
    @ResponseStatus(HttpStatus.OK)
    public void updateMovies(@RequestBody List<Movie> newMovieData) {
        service.updateMovies(newMovieData);
    }


    @DeleteMapping("/movies/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovies(@RequestBody long[] movieIds) {
        service.deleteMovies(movieIds);
    }
}