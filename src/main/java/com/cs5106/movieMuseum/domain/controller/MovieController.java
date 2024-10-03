package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MovieController {
    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Consider error handling? (null values mar shampla)
    // To-Do TESTING!!!!!!!!!!!!!!!!!!!!!!
    // Get request for all movies returns iterable list
    @GetMapping("/movies")
    public Iterable<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);

        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            return ResponseEntity.ok(movie);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    // UTF-8 issue / Bidirectional relationships between entities
    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movieBody) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);

        if (optionalMovie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Movie movie = optionalMovie.get();
        movie.setTitle(movieBody.getTitle());
        movie.setReleaseYear(movieBody.getReleaseYear());
        movie.setImdbRating(movieBody.getImdbRating());
        movie.setDirector(movieBody.getDirector());
        Movie updatedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(updatedMovie);
    }

//    @GetMapping("/movies/findTitle/{title}")
//    public Iterable<Movie> getMovieByTitle(@RequestParam("title") String title) {
//        return movieRepository.findByTitle(title);
//    }

//    @GetMapping(value = "/movies")
//    public Iterable<Movie> getMovieByReleaseYear(@RequestParam("releaseYear") int releaseYear) {
//        return movieRepository.findByReleaseYear(releaseYear);
//    }
//
//    @GetMapping(value = "/movies")
//    public Iterable<Movie> getMovieByImdbRating(@RequestParam("imdbRating") int imdbRating) {
//        return movieRepository.findByImdbRating(imdbRating);
//    }
//
//    @GetMapping(value = "/movies")
//    public Iterable<Movie> getMovieByTitleAndImdbRatingOrderByImdbRatingDesc(@RequestParam("title") String title, @RequestParam("imdbRating") int imdbRating) {
//        return movieRepository.findByTitleAndImdbRatingOrderByImdbRatingDesc(title, imdbRating);
//    }

    @PostMapping("/movies")
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    // Works
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable Long id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Movie movie = optionalMovie.get();
        movieRepository.delete(movie);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movies/title/{title}/releaseYear/{year}")
    public Iterable<Movie> getMovieByTitleAndReleaseYear(@PathVariable String title, @PathVariable int year) {
        return movieRepository.findByTitleAndReleaseYear(title, year);
    }

    @GetMapping("/movies/title/substring/{title}")
    public Iterable<Movie> getMovieByTitleSubstring(@PathVariable String title) {
        return movieRepository.findByTitleSubstring(title);
    }

    @GetMapping("/movies/imdb/between/{lower}/{upper}")
    public Iterable<Movie> getMovieByImdbRatingBetween(@PathVariable double lower, @PathVariable double upper) {
        return movieRepository.findByImdbRatingBetween(lower, upper);
    }
}