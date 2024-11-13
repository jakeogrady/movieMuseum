package com.cs5106.movieMuseum.domain.service;

import com.cs5106.movieMuseum.domain.entity.Genre;
import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.GenreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;

@RestController
public class GenreController {
    private final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
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



    @PostMapping("/genre/post")
    public Genre addGenre(@RequestBody Genre genre) {
        if (genreRepository.findDistinctByGenreName(genre.getGenreName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Genre %s already exists", genre.getGenreName()));
        }
        return genreRepository.save(genre);
    }

    @PutMapping("genres/put")
    @ResponseStatus(HttpStatus.OK)
    public void updateGenres(@RequestBody List<Genre> newGenreData) {
        for (int i = 0; i < newGenreData.size(); i++) {
            Optional<Genre> genreOpt = genreRepository.findById(newGenreData.get(i).getId());
            if (genreOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre with id %d not found", newGenreData.get(i).getId()));
            }
            System.out.println("Updating genre with id " + newGenreData.get(i).getId());
            Genre genre = genreOpt.get();
            genre.setGenreName(newGenreData.get(i).getGenreName());
            genreRepository.save(genre);
        }
    }

    @DeleteMapping("/genres/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenres(@RequestBody long[] genreIds) {
        for (long genreId : genreIds) {
            Optional<Genre> genreOpt = genreRepository.findById(genreId);
            if (genreOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre with id %d not found", genreId));
            }
            System.out.println("Deleting genre with id " + genreId);
            genreRepository.delete(genreOpt.get());
        }
    }
}
