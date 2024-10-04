package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Genre;
import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.GenreRepository;
import com.cs5106.movieMuseum.domain.repository.MovieRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@RestController
public class GenreController {
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    public GenreController(GenreRepository genreRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/genres")
    public Iterable<Genre> getGenres() {
        return genreRepository.findAll();
    }

    @GetMapping("/genre/genreName/{genreName}")
    public Genre getGenreByGenreName(@PathVariable String genreName) {
        Optional<Genre> genreOptional = genreRepository.findDistinctByGenreName(genreName);
        if(genreOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genreName));
        }
        return genreOptional.get();
    }

    @GetMapping("/genres/genreName/substring/{genreName}")
    public List<Genre> getGenresByGenreNameSubstring(@PathVariable String genreName) {
        List<Genre> genres = genreRepository.findGenresByGenreNameSubstring(genreName);
        if (genres.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genreName));
        }
        return genres;
    }

    @GetMapping("/genre/movies/{genre}")
    public Set<Movie> getMoviesByGenre(@PathVariable String genre) {
        Optional<Genre> genreOptional = genreRepository.findDistinctByGenreName(genre);
        if(genreOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genre));
        }
        return genreOptional.get().getMovies();
    }

    @PostMapping("/genre")
    public Genre addGenre(@RequestBody Genre genre) {
        if (genreRepository.findDistinctByGenreName(genre.getGenreName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Genre %s already exists", genre.getGenreName()));
        }
        return genreRepository.save(genre);
    }

    @PutMapping("/genre/{genreName}/addMovie/{title}")
    public ResponseEntity<Set<Genre>> addGenreToMovie(@PathVariable String genreName, @PathVariable String title) {
        Optional<Genre> genreOptional = genreRepository.findDistinctByGenreName(genreName);
        if(genreOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genreName));
        }
        Optional<Movie> movieOptional = movieRepository.findDistinctByTitle(title);
        if(movieOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }

        genreOptional.get().addMovie(movieOptional.get());
        movieOptional.get().addGenre(genreOptional.get());
        genreRepository.save(genreOptional.get());
        movieRepository.save(movieOptional.get());
        return ResponseEntity.ok(movieOptional.get().getGenres());
    }

    @PutMapping("/genre/{genreName}/removeMovie/{title}")
    public ResponseEntity<Set<Genre>> removeGenreFromMovie(@PathVariable String genreName, @PathVariable String title) {
        Optional<Genre> genreOptional = genreRepository.findDistinctByGenreName(genreName);
        if(genreOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genreName));
        }
        Optional<Movie> movieOptional = movieRepository.findDistinctByTitle(title);
        if(movieOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }

        genreOptional.get().removeMovie(movieOptional.get());
        movieOptional.get().removeGenre(genreOptional.get());
        genreRepository.save(genreOptional.get());
        movieRepository.save(movieOptional.get());
        return ResponseEntity.ok(movieOptional.get().getGenres());
    }

    @DeleteMapping("/genre/{genreName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(@PathVariable String genreName) {
        Optional<Genre> genreOptional = genreRepository.findDistinctByGenreName(genreName);
        if(genreOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genreName));
        }
        genreRepository.delete(genreOptional.get());
    }
}
