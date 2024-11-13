package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Genre;
import com.cs5106.movieMuseum.domain.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService genreService) {
        this.service = genreService;
    }

    @GetMapping("/genres")
    public Iterable<Genre> getGenres() {
        return service.getGenres();
    }

    @GetMapping("/genre/genreName/{genreName}")
    public Genre getGenreByGenreName(@PathVariable String genreName) {
        return service.getGenreByGenreName(genreName);
    }

    @GetMapping("/genres/genreName/substring/{genreName}")
    public List<Genre> getGenresByGenreNameSubstring(@PathVariable String genreName) {
        return service.getGenresByGenreNameSubstring(genreName);
    }

    @PostMapping("/genre/post")
    public Genre addGenre(@RequestBody Genre genre) {
        return service.addGenre(genre);
    }

    @PutMapping("genres/put")
    @ResponseStatus(HttpStatus.OK)
    public void updateGenres(@RequestBody List<Genre> newGenreData) {
        service.updateGenres(newGenreData);
    }

    @DeleteMapping("/genres/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenres(@RequestBody long[] genreIds) {
        service.deleteGenres(genreIds);
    }
}
